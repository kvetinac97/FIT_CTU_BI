<?php

namespace App\Service\RelationService;


use App\Entity\Group;
use App\Entity\Room;
use App\Service\AbstractService\PageService\Page;
use App\Service\EntityService\GroupService;
use App\Service\EntityService\ReservationService;
use App\Service\EntityService\RoomService;
use App\Service\EntityService\UserService;
use Symfony\Component\HttpFoundation\InputBag;


class GroupRelationService
{
    private UserService $userService;
    private RoomService $roomService;
    private GroupService $groupService;

    public function __construct(UserService $userService,
                                RoomService $roomService,
                                GroupService $groupService)
    {
        $this->userService = $userService;
        $this->roomService = $roomService;
        $this->groupService = $groupService;
    }

    public function getRoomsPage(Group $group, InputBag $query): Page
    {
        $query->set('group', $group->getId());
        return $this->roomService->getPage($query);
    }

    public function getUsersPage(Group $group, InputBag $query): Page
    {
        $query->set('group', $group->getId());
        return $this->userService->getPage($query);
    }


    public function getOtherUsersPage(Group $group, InputBag $query): Page
    {
        $query->remove('group');
        return $this->userService->getWithoutGroupPage($query, $group);
    }


    public function getOtherRoomsPage(Group $group, InputBag $query): Page
    {
        $query->remove('group');
        return $this->roomService->getWithoutGroupPage($query, $group);
    }


    public function removeUser(Group $group, int $userId)
    {
        if (($user = $this->userService->find($userId)) !== null){
            $this->groupService->removeUsers($group, [$user]);
        }
    }

    public function addUser(Group $group, int $userId, bool $manager = false)
    {
        if (($user = $this->userService->find($userId)) !== null){
            $this->groupService->addMember($group, $user, $manager);
        }
    }

    public function removeRoom(Group $group, int $roomId)
    {
        if (($room = $this->roomService->find($roomId)) !== null){
            $room->setGroup(null);
            $this->roomService->flush();
        }
    }

    public function addRoom(Group $group, int $roomId)
    {
        if (($room = $this->roomService->find($roomId)) !== null){
            $group->addRoom($room);
            $this->roomService->flush();
        }
    }

    public function getGroupMembersPage(Group $group, InputBag $query): array
    {
        $query->set('group', $group->getId());
        $page = $this->userService->getPage($query);

        $users = $page->getData();
        $members = $this->groupService->getMembersFromUsers($group, $users);

        return [$page, $members];
    }

    public function changeUserRole(Group $group, int $changeUserId, bool $manager)
    {
        if (($user = $this->userService->find($changeUserId)) !== null){
            $this->groupService->changeGroupMemberRole($group, $user, $manager);
        }
    }
}
