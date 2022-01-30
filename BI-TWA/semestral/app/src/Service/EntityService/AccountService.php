<?php

namespace App\Service\EntityService;

use App\Entity\Account;
use App\Repository\AccountRepository;
use App\Service\AbstractService\PageService\AbstractPagingService;
use DateTime;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\InputBag;
use Symfony\Component\Security\Core\Exception\UserNotFoundException;

class AccountService extends AbstractPagingService
{
    private EntityManagerInterface $em;

    public function __construct(EntityManagerInterface $entityManager,
                                AccountRepository $repository)
    {
        parent::__construct($repository);
        $this->em = $entityManager;
    }

    public function find(?string $id): ?Account
    {
        return $this->repository->find($id);
    }

    public function create(Account $account)
    {
        $this->em->persist($account);
        $this->flush();
    }


    public function edit(Account $account)
    {
        $this->flush();
    }

    public function remove(Account $account)
    {
        $this->em->remove($account);
        $this->flush();
    }

    public function flush()
    {
        $this->em->flush();
    }

    public function getValidAccountByName (string $name) : Account {
        $result = $this->repository->createQueryBuilder('a')
                ->where('a.name = :name')
                ->andWhere('a.expiration > :now OR a.expiration IS NULL')
                ->setParameter('name', $name)
                ->setParameter('now', new DateTime("now"))
                ->getQuery()->getResult()[0] ?? null;
        if (!$result) throw new UserNotFoundException();
        return $result;
    }

    /*------------ OVERRIDE ABSTRACT METHODS ------------------*/
    protected function getOrderFields(): array
    {
        return ['id'];
    }

    protected function getCriteria(InputBag $query): array
    {
        $criteria = [];

        if (($username = $query->get('name')) && property_exists(Account::class, 'name')){
            $criteria['name'] = $username;
        }

        return $criteria;
    }

    protected function getJoinCriteria(InputBag $query): array
    {
        $criteria = [];

        $user = $query->getInt('user', -1);
        if (($user !== -1) && property_exists(Account::class, 'user'))
        {
            $criteria['user'] = $user;
        }

        return $criteria;
    }
}
