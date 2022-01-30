<?php

namespace App\Service\EntityService;


use App\Entity\Group;
use App\Entity\Reservation;
use App\Entity\Room;
use App\Entity\User;
use App\Repository\UserRepository;
use App\Service\AbstractService\PageService\AbstractPagingService;
use App\Service\AbstractService\PageService\Page;
use App\Service\AbstractService\PageService\PagingQuery;
use App\Service\FileService\ImageService;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\HttpFoundation\InputBag;

class UserService extends AbstractPagingService
{
    private EntityManagerInterface $em;
    private ImageService $is;

    public function __construct(String $uploads,
                                EntityManagerInterface $entityManager,
                                UserRepository $repository)
    {
        parent::__construct($repository);
        $this->em = $entityManager;
        $this->is = new ImageService($uploads, 'ava.png');
    }

    public function find(?string $id): ?User
    {
        return $this->repository->find($id);
    }

    public function create(User $user,?UploadedFile $image = null )
    {
        $name = $this->is->upload($image);
        $user->setImage($name);

        $this->em->persist($user);
        $this->flush();
    }


    public function edit(User $user, ?UploadedFile $image = null)
    {
        if ($image !== null){
            $name = $this->is->upload($image);
            $user->setImage($name);
        }

        $this->flush();
    }

    public function remove(User $user)
    {
        $imageName = $user->getImage();
        $this->is->remove($imageName);

        $this->em->remove($user);
        $this->flush();
    }

    public function flush()
    {
        $this->em->flush();
    }

    public function getWithoutReservationPage(InputBag $query, Reservation $reservation): Page
    {
        $pagingQuery = new PagingQuery($this->repository);

        $reservationId = $reservation->getId();
        $pagingQuery->addNegativeSearchByOtherEntityManyToMany(['reservations' => $reservationId]);

        return $this->getPage($query, $pagingQuery);
    }

    public function getWithoutGroupPage(InputBag $query, Group $group): Page
    {
        $pagingQuery = new PagingQuery($this->repository);

        $groupId = $group->getId();
        $pagingQuery->addNegativeDecompositionSearch(['group' => ['groupMemberships', $groupId]]);

        return $this->getPage($query, $pagingQuery);
    }

    public function getWithoutRoomPage(InputBag $query, Room $room): Page
    {
        $pagingQuery = new PagingQuery($this->repository);

        $roomId = $room->getId();
        $pagingQuery->addNegativeDecompositionSearch(['room' => ['roomMemberships', $roomId]]);

        return $this->getPage($query, $pagingQuery);
    }


    /*------------ OVERRIDE ABSTRACT METHODS ------------------*/
    protected function getOrderFields(): array
    {
        return ['name', 'email'];
    }

    protected function getJoinCriteria(InputBag $query): array
    {
        $criteria = [];

        $reservation = $query->getInt('reservation', -1);
        if (($reservation !== -1) && property_exists(User::class, 'reservations'))
        {
            $criteria['reservations'] = $reservation;
        }

        return $criteria;
    }

    protected function getCriteria(InputBag $query): array
    {
        $criteria = [];

        if (($name = $query->get('name')) && property_exists(User::class, 'name')){
            $criteria['name'] = $name;
        }

        return $criteria;
    }

    protected function getDecompositionJoinCriteria(InputBag $query): array
    {
        $criteria = [];

        $group = $query->getInt('group', -1);
        if (($group !== -1) && property_exists(User::class, 'groupMemberships'))
        {
            $criteria['group'] = ['groupMemberships', $group];
        }

        $room = $query->getInt('room', -1);
        if (($room !== -1) && property_exists(User::class, 'roomMemberships'))
        {
            $criteria['room'] = ['roomMemberships', $room];
        }

        return $criteria;
    }
}
