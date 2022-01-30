<?php

namespace App\DataFixtures;

use App\Entity\Building;
use App\Service\EntityService\BuildingService;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class BuildingFixtures extends Fixture
{
    public const BUILDING_REFERENCE = 'building';

    private BuildingService $service;

    public function __construct(BuildingService $buildingService)
    {
        $this->service = $buildingService;
    }

    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create('en_US');

        for ($i = 0; $i <= 10; ++$i){
            $building = new Building();
            $building->setName($faker->regexify('[A-Z][A-Z][A-Z]-[0-9][0-9]'));
            $this->service->create($building);
            $this->addReference(self::BUILDING_REFERENCE . $i, $building);
        }
    }
}
