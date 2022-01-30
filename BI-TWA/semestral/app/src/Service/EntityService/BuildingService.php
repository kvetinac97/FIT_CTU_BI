<?php

namespace App\Service\EntityService;

use App\Entity\Building;
use App\Repository\BuildingRepository;
use App\Service\AbstractService\PageService\AbstractPagingService;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\InputBag;

class BuildingService extends AbstractPagingService
{
    private EntityManagerInterface $em;

    public function __construct(EntityManagerInterface $entityManager,
                                BuildingRepository $repository)
    {
        parent::__construct($repository);
        $this->em = $entityManager;
    }

    public function find(?string $id): ?Building
    {
        return $this->repository->find($id);
    }

    public function create(Building $building)
    {
        $this->em->persist($building);
        $this->flush();
    }


    public function edit(Building $building)
    {
        $this->flush();
    }

    public function remove(Building $building)
    {
        $this->em->remove($building);
        $this->flush();
    }

    public function flush()
    {
        $this->em->flush();
    }

    /*------------ OVERRIDE ABSTRACT METHODS ------------------*/
    protected function getOrderFields(): array
    {
        return ['name'];
    }

    protected function getJoinCriteria(InputBag $query): array
    {
        $criteria = [];

        $room = $query->getInt('room', -1);
        if (($room !== -1) && property_exists(Building::class, 'rooms'))
        {
            $criteria['rooms'] = $room;
        }

        return $criteria;
    }

    protected function getCriteria(InputBag $query): array
    {
        $criteria = [];

        if (($name = $query->get('name')) && property_exists(Building::class, 'name')){
            $criteria['name'] = $name;
        }

        return $criteria;
    }
}