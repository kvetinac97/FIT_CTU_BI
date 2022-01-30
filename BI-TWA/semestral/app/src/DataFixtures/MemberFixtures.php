<?php

namespace App\DataFixtures;

use App\Entity\Group;
use App\Entity\User;
use App\Entity\Member;
use App\DataFixtures\GroupFixtures;
use App\DataFixtures\UserFixtures;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class MemberFixtures extends Fixture implements DependentFixtureInterface
{

    public function getDependencies()
    {
        return [
            GroupFixtures::class,
            UserFixtures::class
        ];
    }

    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create('en_US');

        for ($i = 0; $i <= 10; ++$i){
            $rand = $faker->numberBetween($min = 0, $max = 50);
            for ($j = 0; $j < $faker->numberBetween($min = 0, $max = 15); ++$j){
                $member = new Member();
                $member->setGroup($this->getReference(GroupFixtures::GROUP_REFERENCE . $i));
                $member->setUser($this->getReference(UserFixtures::USER_REFERENCE . ($rand + $j)%50));
                $member->setManager($faker->boolean($chanceOfGettingTrue = 10));
                $manager->persist($member);
                $manager->flush();
            }
        }
    }
}
