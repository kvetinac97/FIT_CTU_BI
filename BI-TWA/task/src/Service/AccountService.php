<?php

namespace App\Service;

use App\Entity\Account;
use App\Entity\Employee;
use App\Repository\AccountRepository;
use DateTime;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Security\Core\Exception\UserNotFoundException;

class AccountService {

    private EntityManagerInterface $entityManager;
    private AccountRepository $accountRepository;

    public function __construct (EntityManagerInterface $entityManager,
                                 AccountRepository $accountRepository) {
        $this->entityManager = $entityManager;
        $this->accountRepository = $accountRepository;
    }

    public function getAccount (Employee $employee, string $name, &$account) : bool {
        $acc = $employee->getAccounts()
            ->filter(fn(Account $account) => $account->getName() === $name)
            ->first();

        if ($acc !== false) {
            $account = $acc;
            return true;
        }
        return false;
    }

    public function getValidAccountByName (string $name) : ?Account {
        $result = $this->accountRepository->createQueryBuilder('a')
            ->where('a.name = :name')
            ->andWhere('a.expiration > :now OR a.expiration IS NULL')
            ->setParameter('name', $name)
            ->setParameter('now', new DateTime("now"))
            ->getQuery()->getResult()[0] ?? null;
        if (!$result) throw new UserNotFoundException();
        return $result;
    }

    /** @return Account[] */
    public function getAccounts (Employee $employee) : array {
        return $employee->getAccounts()->toArray();
    }

    public function addAccount (Account $account) {
        $this->entityManager->persist($account);
        $this->entityManager->flush();
    }

    public function deleteAccount (Account $account) {
        $this->entityManager->remove($account);
        $this->entityManager->flush();
    }

}
