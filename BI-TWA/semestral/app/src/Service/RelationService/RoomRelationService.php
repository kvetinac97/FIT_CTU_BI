<?php

namespace App\Service\RelationService;


use App\Entity\Room;
use App\Entity\User;
use App\Service\AbstractService\PageService\Page;
use App\Service\AbstractService\PageService\PagingQuery;
use App\Service\EntityService\GroupService;
use App\Service\EntityService\ReservationService;
use App\Service\EntityService\RoomService;
use App\Service\EntityService\RoomUserService;
use App\Service\EntityService\UserService;
use Symfony\Component\HttpFoundation\InputBag;


class RoomRelationService
{
    private UserService $userService;
    private GroupService $groupService;
    private ReservationService $reservationService;
    private RoomService $roomService;

    public function __construct(RoomService $roomService,
                                UserService $userService,
                                GroupService $groupService,
                                ReservationService $reservationService)
    {
        $this->roomService = $roomService;
        $this->userService = $userService;
        $this->groupService = $groupService;
        $this->reservationService = $reservationService;
    }

    public function getGroupsPage(Room $room, InputBag $query): Page
    {
        $query->set('room', $room->getId());
        return $this->groupService->getPage($query);
    }

    public function getReservationsPage(Room $room, InputBag $query): Page
    {
        $query->set('room', $room->getId());
//        $query->set('approved', true);

        return $this->reservationService->getRoomReservationsPage($query);
    }

    public function getRoomUsersPage(Room $room, InputBag $query): array
    {
        $query->set('room', $room->getId());
        $page = $this->userService->getPage($query);

        $users = $page->getData();
        $members = $this->roomService->getMembersFromUsers($room, $users);

        return [$page, $members];
    }

    public function removeUser(Room $room, int $userId)
    {
        if (($user = $this->userService->find($userId)) !== null){
            $this->roomService->removeRoomUser($room, $user);
        }
    }

    public function addUser(Room $room, int $userId, bool $manager = false)
    {
        if (($user = $this->userService->find($userId)) !== null){
            $this->roomService->addRoomUser($room, $user, $manager);
        }
    }

    public function getOtherUsersPage(Room $room, InputBag $query): Page
    {
        $query->remove('user');
        return $this->userService->getWithoutRoomPage($query, $room);
    }

    public function changeUserRole(Room $room, int $changeUserId, bool $manager)
    {
        if (($user = $this->userService->find($changeUserId)) !== null){
            $this->roomService->changeRoomUserRole($room, $user, $manager);
        }
    }
}
