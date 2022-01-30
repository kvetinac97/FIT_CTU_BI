<?php

namespace App\DataFixtures;

use App\Entity\Account;
use App\Entity\User;
use App\Service\EntityService\UserService;
use App\Service\EntityService\AccountService;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class UserFixtures extends Fixture
{
    public const USER_REFERENCE = 'user';
    private UserService $uservice;
    private AccountService $aservice;
    private UserPasswordHasherInterface $hasher;

    public function __construct(
        UserService $userService,
        AccountService $accountService,
        UserPasswordHasherInterface $hasher)
    {
        $this->uservice = $userService;
        $this->aservice = $accountService;
        $this->hasher = $hasher;
    }


    // https://fakerphp.github.io/
    // https://symfony.com/bundles/DoctrineFixturesBundle/current/index.html
    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create('en_US');

        for ($i = 0; $i <= 50; ++$i){
            $user = new User();
            $user->setName($faker->name());
            $user->setEmail($faker->safeEmail());
            $user->setTel($faker->regexify('[0-9][0-9][0-9] [0-9][0-9][0-9] [0-9][0-9][0-9]'));

            $account = new Account();

            if ($i === 0){
                $account->setName('user');
                $account->setPassword($this->hasher->hashPassword($account, 'password'));
                $account->setRoles(['ROLE_USER']);
            }
            else if ($i === 1){
                $account->setName('admin');
                $account->setPassword($this->hasher->hashPassword($account, 'password'));
                $account->setRoles(['ROLE_ADMIN']);
            }
            else {
                $account->setName($faker->name());
                $account->setRoles($faker->boolean() ? ['ROLE_USER'] : ['ROLE_ADMIN', 'ROLE_USER']);
                $account->setPassword($this->hasher->hashPassword($account, $faker->password()));
            }

            $manager->persist($user);
            $manager->persist($account);
            $user->addAccount($account);
            $this->uservice->create($user);
            $this->addReference(self::USER_REFERENCE . $i, $user);
        }
    }
}