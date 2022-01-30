<?php

namespace App\DataFixtures;

use App\Entity\Room;
use App\Service\EntityService\RoomService;
use App\DataFixtures\BuildingFixtures;
use App\DataFixtures\GroupFixtures;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class RoomFixtures extends Fixture implements DependentFixtureInterface
{
    private RoomService $service;
    public const ROOM_REFERENCE = 'room';

    public function __construct(RoomService $roomService)
    {
        $this->service = $roomService;
    }

    public function getDependencies()
    {
        return [
            BuildingFixtures::class,
            GroupFixtures::class
        ];
    }

    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create('en_US');

        for ($i = 0; $i <= 30; ++$i){
            $room = new Room();
            $room->setName($faker->regexify('[0-9][0-9][0-9][0-9]'));
            $room->setPublic($faker->boolean($chanceOfGettingTrue = 75));
            $room->setOpen($faker->boolean($chanceOfGettingTrue = 75));
            $room->setBuilding($this->getReference(BuildingFixtures::BUILDING_REFERENCE . $faker->numberBetween($min = 0, $max = 10)));
            $room->setGroup($this->getReference(GroupFixtures::GROUP_REFERENCE . $faker->numberBetween($min = 0, $max = 10)));
            $this->service->create($room);
            $this->addReference(self::ROOM_REFERENCE . $i, $room);
        }
    }
}
