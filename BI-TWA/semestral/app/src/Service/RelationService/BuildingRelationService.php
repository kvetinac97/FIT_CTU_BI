<?php

namespace App\Service\RelationService;

use App\Entity\Building;
use App\Entity\Room;
use App\Entity\User;
use App\Service\AbstractService\PageService\Page;
use App\Service\EntityService\RoomService;
use Symfony\Component\HttpFoundation\InputBag;

class BuildingRelationService
{
    private RoomService $roomService;

    public function __construct(RoomService $roomService)
    {
        $this->roomService = $roomService;
    }

    public function getRoomsPage(Building $building, InputBag $query): Page
    {
        $query->set('building', $building->getId());
        return $this->roomService->getPage($query);
    }

}