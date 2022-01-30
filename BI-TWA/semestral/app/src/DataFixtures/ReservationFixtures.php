<?php

namespace App\DataFixtures;

use App\Entity\Room;
use App\Entity\User;
use App\Entity\Reservation;
use App\DataFixtures\RoomFixtures;
use App\DataFixtures\UserFixtures;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class ReservationFixtures extends Fixture implements DependentFixtureInterface
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
            for ($j = 0; $j < $faker->numberBetween($min = 0, $max = 5); ++$j){
                $reservation = new Reservation();
                $reservation->setApproved($faker->boolean($chanceOfGettingTrue = 25));
                $reservation->setCancelled($faker->boolean($chanceOfGettingTrue = 10));
                $reservation->setStart($faker->dateTimeBetween($startDate = 'now', $endDate = '+ 1 months', $timezone = null));
                $reservation->setUntil($faker->dateTimeBetween($reservation->getStart(),$reservation->getStart()->getTimeStamp() + 10000));
                $reservation->setRoom($this->getReference(RoomFixtures::ROOM_REFERENCE . $i));
                $reservation->setAuthor($this->getReference(UserFixtures::USER_REFERENCE . ($rand + $j)%50));
                for ($k = 0; $k < $faker->numberBetween($min = 0, $max = 10); ++$k){
                    $reservation->addUser($this->getReference(UserFixtures::USER_REFERENCE . $faker->numberBetween($min = 0, $max = 50)));
                }
                $manager->persist($reservation);
                $manager->flush();
            }
        }
    }
}
