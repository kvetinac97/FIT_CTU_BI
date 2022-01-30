<?php

namespace App\DataFixtures;

use App\Entity\Group;
use App\Service\EntityService\GroupService;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class GroupFixtures extends Fixture
{
    public const GROUP_REFERENCE = 'group';
    private GroupService $service;

    public function __construct(GroupService $groupService)
    {
        $this->service = $groupService;
    }

    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create('en_US');

        for ($i = 0; $i <= 10; ++$i){
            $group = new Group();
            $group->setName($faker->regexify('[A-Z][A-Z]/[0-9][0-9]'));
            $this->service->create($group);
            if ($faker->boolean() && $i != 0)
                $group->setParentGroup($this->getReference(self::GROUP_REFERENCE . $faker->numberBetween($min = 0, $max = $i-1)));
            $this->addReference(self::GROUP_REFERENCE . $i, $group);
        }
    }
}
