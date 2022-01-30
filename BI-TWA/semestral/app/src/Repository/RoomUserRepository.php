<?php

namespace App\Repository;

use App\Entity\RoomUser;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method RoomUser|null find($id, $lockMode = null, $lockVersion = null)
 * @method RoomUser|null findOneBy(array $criteria, array $orderBy = null)
 * @method RoomUser[]    findAll()
 * @method RoomUser[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class RoomUserRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, RoomUser::class);
    }
}
