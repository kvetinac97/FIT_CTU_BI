<?php

namespace App\Service\EntityService;

use App\Entity\Group;
use App\Entity\Member;
use App\Entity\Room;
use App\Entity\RoomUser;
use App\Entity\User;
use App\Repository\MemberRepository;
use App\Repository\RoomUserRepository;
use Doctrine\ORM\EntityManagerInterface;


class MemberService
{
    private EntityManagerInterface $em;
    private MemberRepository $repository;

    public function __construct(EntityManagerInterface $entityManager,
                                MemberRepository $memberRepository)
    {
        $this->em = $entityManager;
        $this->repository = $memberRepository;
    }

    public function find(?string $id): ?Member
    {
        return $this->repository->find($id);
    }

    public function edit(){
        $this->flush();
    }

    public function remove(Member $member)
    {
        $this->em->remove($member);
        $this->flush();
    }

    public function flush()
    {
        $this->em->flush();
    }

    public function findBy(Group $group, User $user): ?Member
    {
        return $this->repository->findOneBy(['user' => $user, 'group' => $group]);
    }

    public function create(Member $member)
    {
        $this->em->persist($member);
        $this->flush();
    }
}
