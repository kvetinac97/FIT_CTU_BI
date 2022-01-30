<?php

namespace App\Service\EntityService;

use App\Entity\Room;
use App\Entity\RoomUser;
use App\Entity\User;
use App\Repository\RoomUserRepository;
use Doctrine\ORM\EntityManagerInterface;


class RoomUserService
{
    private EntityManagerInterface $em;
    private RoomUserRepository $repository;

    public function __construct(EntityManagerInterface $entityManager,
                                RoomUserRepository $roomUserRepository)
    {
        $this->em = $entityManager;
        $this->repository = $roomUserRepository;
    }

    public function find(?string $id): ?RoomUser
    {
        return $this->repository->find($id);
    }

    public function edit(){
        $this->flush();
    }

    public function remove(RoomUser $roomUser)
    {
        $this->em->remove($roomUser);
        $this->flush();
    }

    public function flush()
    {
        $this->em->flush();
    }

    public function findBy(Room $room, User $user): ?RoomUser
    {
        return $this->repository->findOneBy(['user' => $user, 'room' => $room]);
    }

    public function create(RoomUser $roomUser)
    {
        $this->em->persist($roomUser);
        $this->flush();
    }
}
