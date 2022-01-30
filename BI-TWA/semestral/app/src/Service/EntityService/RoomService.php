<?php

namespace App\Service\EntityService;


use App\Entity\Account;
use App\Entity\Group;
use App\Entity\Reservation;
use App\Entity\Room;
use App\Entity\RoomUser;
use App\Entity\User;
use App\Repository\MemberRepository;
use App\Repository\ReservationRepository;
use App\Repository\RoomRepository;
use App\Repository\RoomUserRepository;
use App\Service\AbstractService\PageService\AbstractPagingService;
use App\Service\AbstractService\PageService\Page;
use App\Service\AbstractService\PageService\PagingQuery;
use DateTime;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\ORM\Tools\Pagination\Paginator;
use Symfony\Component\HttpFoundation\InputBag;

class RoomService extends AbstractPagingService
{
    private EntityManagerInterface $em;
    private RoomUserRepository $roomUserRepository;
    private ReservationRepository $reservationRepository;

    public function __construct(EntityManagerInterface $entityManager,
                                RoomRepository $repository,
                                RoomUserRepository $roomUserRepository,
                                ReservationRepository $reservationRepository)
    {
        parent::__construct($repository);
        $this->em = $entityManager;
        $this->roomUserRepository = $roomUserRepository;
        $this->reservationRepository = $reservationRepository;
    }


    public function find(?string $id): ?Room
    {
        return $this->repository->find($id);
    }

    public function create(Room $room)
    {
        $this->em->persist($room);
        $this->flush();
    }


    public function edit(Room $room)
    {
        $this->flush();
    }

    public function remove(Room $room)
    {
        $this->em->remove($room);
        $this->flush();
    }

    public function flush()
    {
        $this->em->flush();
    }

    /** @return int[] */
    public function getAdminRooms ($account) : array {
        if (!$account instanceof Account) return [];

        // Admin can do anything
        if (in_array('ROLE_ADMIN', $account->getRoles()))
            return array_map(fn(Room $room) => $room->getId(), $this->repository->findAll());

        // User must be member - manager to admin a room
        $user = $account->getUser();
        return $user->getRoomMemberships()
            ->filter(fn(RoomUser $member) => $member->getManager())
            ->map(fn(RoomUser $member) => $member->getRoom()->getId())->toArray();
    }

    /*
     * Funkce zjisti, zda ma dany clovek pristup do mistnosti
     * to nastane tehdy, pokud:
     * 1. je uzivatelem dane mistnosti
     * 2. je clenem skupiny, ktere dana mistnost patri
     * 3. je dana mistnost odemcena
     * 4. existuje platna rezervace (casove, schvalena, nezrusena) dane mistnosti,
     *    jejiz soucasti je i dany clovek
     */
    public function hasAccess (Room $room, User $user) : bool
    {
        // Podmínka (1)
        if ( $room->getMembers()->exists(fn(int $id) =>
            $room->getMembers()->get($id)
                ->getUser()->getId() === $user->getId())
        ) return true;

        // Podmínka (2)
        if ( $room->getGroup() !== null && $room->getGroup()->getMembers()->exists(fn(int $id) =>
            $room->getGroup()->getMembers()->get($id)
                ->getUser()->getId() === $user->getId())
        ) return true;

        // Podmínka (3)
        if ( $room->getOpen() ) return true;

        // Podmínka (4)
        $now = new DateTime();
        $actualReservations = $room->getReservations()->filter(fn(Reservation $reservation) =>
            $reservation->getRoom()->getId() === $room->getId() && $reservation->getApproved() &&
            !$reservation->getCancelled() && $reservation->getStart() <= $now && $now <= $reservation->getUntil());

        // Existují rezervace => uživatel musí být součástí alespoň jedné
        return $actualReservations->exists(fn(int $id) =>
            $actualReservations->get($id)
                ->getUsers()
                ->exists(fn(int $userId) =>
                    $actualReservations->get($id)->getUsers()
                        ->get($userId)->getId() === $user->getId()
                )
        );
    }

    public function getWithoutGroupPage(InputBag $query, Group $group): Page
    {
        $pagingQuery = new PagingQuery($this->repository);

        $groupId = $group->getId();
        $pagingQuery->addNegativeSearchByOtherEntityManyToMany(['group' => $groupId]);

        return $this->getPage($query, $pagingQuery);
    }

    public function removeRoomUser(Room $room, ?User $user)
    {
        $roomUser = $this->roomUserRepository->findOneBy(['user' => $user, 'room' => $room]);
        if ($roomUser !== null){
            $room->getMembers()->removeElement($roomUser);
            $this->flush();
        }
    }

    public function addRoomUser(Room $room, ?User $user, $manager = false)
    {
        $roomUser = $this->roomUserRepository->findOneBy(['user' => $user, 'room' => $room]);
        if ($roomUser === null){
            $roomUser = new RoomUser();
            $roomUser->setRoom($room);
            $roomUser->setUser($user);
            $roomUser->setManager($manager);
            $this->em->persist($roomUser);
            $this->flush();
        }
    }

    public function getMembersFromUsers(Room $room, Paginator $users): array
    {
        return $this->roomUserRepository->findBy(['room' => $room, 'user' => $users->getIterator()->getArrayCopy()]);
    }

    public function changeRoomUserRole(Room $room, ?User $user, bool $manager)
    {
        $roomUser = $this->roomUserRepository->findOneBy(['user' => $user, 'room' => $room]);
        if ($roomUser !== null && $roomUser->getManager() !== $manager){
            $roomUser->setManager($manager);
            $this->flush();
        }
    }


    public function dtoArray(Paginator $data): array
    {
        $res = [];
        foreach ($data as $item) { $res[] = ['room' => $item]; }
        return $res;
    }

    public function addManagerProperty(array $data): array
    {
        $res = [];
        foreach($data as $item){
            $room = $item["room"];
            $manager = $this->roomUserRepository->findOneBy(['room' => $room, 'manager' => true]);
            $res[] = $item + ["manager" => $manager];
        }

        return $res;
    }

    public function addTotalReservationsProperty(array $data): array
    {
        $res = [];
        foreach($data as $item){
            $room = $item["room"];
            $totalReservations = $this->reservationRepository->count(['room'=>$room, 'approved' => true]);
            $res[] = $item + ["totalReservations" => $totalReservations];
        }

        return $res;
    }


    /*------------ OVERRIDE ABSTRACT METHODS ------------------*/
    protected function getOrderFields(): array
    {
        return ['name'];
    }

    protected function getJoinCriteria(InputBag $query): array
    {
        $criteria = [];

        $building = $query->getInt('building', -1);
        if (($building !== -1) && property_exists(Room::class, 'building'))
        {
            $criteria['building'] = $building;
        }

        $group = $query->getInt('group', -1);
        if (($group !== -1) && property_exists(Room::class, 'group'))
        {
            $criteria['group'] = $group;
        }

        return $criteria;
    }

    protected function getCriteria(InputBag $query): array
    {
        $criteria = [];

        if (($name = $query->get('name')) && property_exists(Room::class, 'name')){
            $criteria['name'] = $name;
        }

        if(($public = $query->get('public')) && property_exists(Room::class, 'public')){
            $criteria['public'] = $public;
        }

        return $criteria;
    }

    protected function getDecompositionJoinCriteria(InputBag $query): array
    {
        $criteria = [];

        $user = $query->getInt('user', -1);
        if (($user !== -1) && property_exists(Room::class, 'members'))
        {
            $criteria['user'] = ['members', $user];
        }

        return $criteria;
    }
}
