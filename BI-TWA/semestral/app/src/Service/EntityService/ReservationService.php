<?php

namespace App\Service\EntityService;

use App\Entity\Account;
use App\Entity\Group;
use App\Entity\Member;
use App\Entity\Reservation;
use App\Entity\Room;
use App\Entity\RoomUser;
use App\Entity\User;
use App\Repository\ReservationRepository;
use App\Service\AbstractService\PageService\AbstractPagingService;
use App\Service\AbstractService\PageService\Page;
use App\Service\AbstractService\PageService\PagingQuery;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\InputBag;
use function Symfony\Component\Translation\t;

class ReservationService extends AbstractPagingService
{
    private EntityManagerInterface $em;

    public function __construct(EntityManagerInterface $entityManager,
                                ReservationRepository $repository)
    {
        parent::__construct($repository);
        $this->em = $entityManager;
    }

    public function find(?string $id): ?Reservation
    {
        return $this->repository->find($id);
    }

    public function create(Reservation $reservation)
    {
        $this->em->persist($reservation);
        $this->flush();
    }


    public function edit(Reservation $reservation)
    {
        $this->flush();
    }

    public function remove(Reservation $reservation)
    {
        $this->em->remove($reservation);
        $this->flush();
    }

    public function flush()
    {
        $this->em->flush();
    }

    /** @return int[] */
    public function getPossibleReservationRooms ($account) : array {
        if (!$account instanceof Account) return [];

        // Admin can do anything
        if (in_array('ROLE_ADMIN', $account->getRoles()))
            return array_map(fn(Reservation $reservation) => $reservation->getRoom()->getId(), $this->repository->findAll());

        // User can reserve rooms which he is RoomUser of
        $user = $account->getUser();
        $rooms = $user->getRoomMemberships()->map(fn(RoomUser $ru) => $ru->getRoom()->getId())->toArray();

        // User can reserve rooms which belong to group he is Member of (+ transitively)
        $groups = $user->getGroupMemberships()->map(fn(Member $member) => $member->getGroup())->toArray();

        /** @var Group $group */
        while (($group = array_shift($groups)) !== null) {
            $rooms = array_merge($rooms, $group->getRooms()->map(fn(Room $room) => $room->getId())->toArray());
            $groups = array_merge($groups, $group->getChildren()->toArray());
        }
        // as there are no cycles, no need to track visited nodes

        return array_unique($rooms);
    }

    /** @return int[] */
    public function getPossibleAdminRooms ($account) : array {
        if (!$account instanceof Account) return [];

        // Admin can do anything
        if (in_array('ROLE_ADMIN', $account->getRoles()))
            return array_map(fn(Reservation $reservation) => $reservation->getRoom()->getId(), $this->repository->findAll());

        // User can approve reservation of rooms which he is RoomUser && manager of
        $user = $account->getUser();
        $rooms = $user->getRoomMemberships()->filter(fn(RoomUser $ru) => $ru->getManager())
            ->map(fn(RoomUser $ru) => $ru->getRoom()->getId())->toArray();

        // User can reserve rooms which belong to group he is Member && manager of (+ transitively)
        $groups = $user->getGroupMemberships()->filter(fn(Member $member) => $member->getManager())
            ->map(fn(Member $member) => $member->getGroup())->toArray();

        /** @var Group $group */
        while (($group = array_shift($groups)) !== null) {
            $rooms = array_merge($rooms, $group->getRooms()->map(fn(Room $room) => $room->getId())->toArray());
            $groups = array_merge($groups, $group->getChildren()->toArray());
        }
        // as there are no cycles, no need to track visited nodes

        return array_unique($rooms);
    }

    public function getUserReservationsPage(InputBag $query, Account $account): Page
    {
        $pagingQuery = new PagingQuery($this->repository);

        switch ($query->get('status', 'all')) {
            case 'approved':
                $pagingQuery->addSimpleSearch(['approved' => true], false);
                break;
            case 'cancelled':
                $pagingQuery->addSimpleSearch(['cancelled' => true], false);
                break;
            case 'waiting':
                $pagingQuery->addSimpleSearch(['approved' => false, 'cancelled' => false], false);
                break;
        }

        switch ($query->get('relation', 'participant')) {
            // Author Only
            case 'author':
                $pagingQuery->addSimpleSearch(['author' => $account->getUser()->getId()], false);
                break;
            // Participant only
            case 'participant':
            default:
                $userId = $account->getUser()->getId();
                $pagingQuery->addSearchByOtherEntity(['users' => $userId]);
                break;
        }

        return $this->getPage($query, $pagingQuery);
    }

    public function getRoomReservationsPage(InputBag $query): Page
    {
        $pagingQuery = new PagingQuery($this->repository);

        switch ($query->get('status', 'all')) {
            case 'approved':
                $pagingQuery->addSimpleSearch(['approved' => true], false);
                break;
            case 'cancelled':
                $pagingQuery->addSimpleSearch(['cancelled' => true], false);
                break;
            case 'waiting':
                $pagingQuery->addSimpleSearch(['approved' => false, 'cancelled' => false], false);
                break;
        }

        return $this->getPage($query, $pagingQuery);
    }

    public function getManagerReservationsPage(InputBag $query, Account $account): Page
    {
        $pagingQuery = new PagingQuery($this->repository);

        // Reservations visible for group/room manager
        $adminRooms = $this->getPossibleAdminRooms($account);
        $pagingQuery->addSimpleSearchMany(['room' => $adminRooms]);

        return $this->getPage($query, $pagingQuery);
    }

    public function getNonAdminReservationsPage(InputBag $query, Account $account): Page
    {
        $pagingQuery = new PagingQuery($this->repository);

        // Author
        $pagingQuery->orSimpleSearch(['author' => $account->getUser()->getId()]);

        // Participant
        $userId = $account->getUser()->getId();
        $pagingQuery->orSearchByOtherEntity(['users' => $userId]);

        // Group Manager
        $adminRooms = $this->getPossibleAdminRooms($account);
        $pagingQuery->orSimpleSearchMany(['room' => $adminRooms]);

        dump($pagingQuery);
        return $this->getPage($query, $pagingQuery);
    }



    /*------------ OVERRIDE ABSTRACT METHODS ------------------*/
    protected function getOrderFields(): array
    {
        return ['id'];
    }

    protected function getCriteria(InputBag $query): array
    {
        $criteria = [];
        if (($approved = $query->get('approved')) && property_exists(Reservation::class, 'approved')){
            $criteria['approved'] = $approved;
        }
        return $criteria;
    }

    protected function getPageCriteria(InputBag $query): array
    {
        $criteria = [];

        if ($relation = $query->get('relation', false))
            $criteria['relation'] = $relation;
        if ($status = $query->get('status', false))
            $criteria['status'] = $status;

        return array_merge(parent::getPageCriteria($query), $criteria);
    }

    protected function getDateCriteria(InputBag $query): array
    {
        $criteria = [];

        if (($start = $query->get('from')) && property_exists(Reservation::class, 'start')){
            $criteria['start'] = [$start, false];
        }

        if (($until = $query->get('until')) && property_exists(Reservation::class, 'until')){
            $criteria['until'] = [$until, true];
        }

        return $criteria;
    }

    protected function getJoinCriteria(InputBag $query): array
    {
        $criteria = [];

        $author = $query->getInt('author', -1);
        if (($author !== -1) && property_exists(Reservation::class, 'author'))
        {
            $criteria['author'] = $author;
        }

        $room = $query->getInt('room', -1);
        if (($room !== -1) && property_exists(Reservation::class, 'room'))
        {
            $criteria['room'] = $room;
        }

        return $criteria;
    }
}