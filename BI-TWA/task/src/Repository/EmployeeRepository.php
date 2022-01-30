<?php

namespace App\Repository;

use App\Entity\Employee;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method Employee|null find($id, $lockMode = null, $lockVersion = null)
 * @method Employee|null findOneBy(array $criteria, array $orderBy = null)
 * @method Employee[]    findAll()
 * @method Employee[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class EmployeeRepository extends ServiceEntityRepository {

    public function __construct (ManagerRegistry $registry) {
        parent::__construct($registry, Employee::class);
    }

    public function getByUsername ($value) : ?Employee {
        return $this->createQueryBuilder('e')
            ->andWhere('e.username = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult();
    }

    /** @return Employee[] */
    public function findByName ($value) : array {
        return $this->createQueryBuilder('e')
            ->andWhere('e.name LIKE :val')
            ->setParameter('val', '%' . $value . '%')
            ->leftJoin('e.roles', 'role')
            ->addSelect('role')
            ->getQuery()
            ->getResult();
    }

}
