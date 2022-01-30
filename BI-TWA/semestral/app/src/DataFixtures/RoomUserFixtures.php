<?php

namespace App\DataFixtures;

use App\Entity\Room;
use App\Entity\User;
use App\Entity\RoomUser;
use App\DataFixtures\RoomFixtures;
use App\DataFixtures\UserFixtures;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class RoomUserFixtures extends Fixture implements DependentFixtureInterface
{

    public function getDependencies()
    {
        return [
            RoomFixtures::class,
            UserFixtures::class
        ];
    }

    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create('en_US');

        for ($i = 0; $i <= 30; ++$i){
            $rand = $faker->numberBetween($min = 0, $max = 50);
            for ($j = 0; $j < $faker->numberBetween($min = 0, $max = 10); ++$j){
                $roomUser = new RoomUser();
                $roomUser->setRoom($this->getReference(RoomFixtures::ROOM_REFERENCE . $i));
                $roomUser->setUser($this->getReference(UserFixtures::USER_REFERENCE . ($rand + $j)%50));
                $roomUser->setManager($faker->boolean($chanceOfGettingTrue = 10));
                $manager->persist($roomUser);
                $manager->flush();
            }
        }
    }
}
