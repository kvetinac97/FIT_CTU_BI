<?php

namespace App\Service\RelationService;

use App\Entity\Account;
use App\Entity\User;
use App\Service\AbstractService\PageService\Page;
use App\Service\EntityService\AccountService;
use App\Service\EntityService\GroupService;
use App\Service\EntityService\ReservationService;
use App\Service\EntityService\RoomService;
use Symfony\Component\HttpFoundation\InputBag;


class UserRelationService
{
    private AccountService $accountService;
    private RoomService $roomService;
    private GroupService $groupService;
    private ReservationService $reservationService;

    public function __construct(AccountService $accountService,
                                RoomService $roomService,
                                GroupService $groupService,
                                ReservationService $reservationService)
    {
        $this->accountService = $accountService;
        $this->roomService = $roomService;
        $this->groupService = $groupService;
        $this->reservationService = $reservationService;
    }

    public function getGroupsPage(User $user, InputBag $query): Page
    {
        $query->set('user', $user->getId());
        return $this->groupService->getPage($query);
    }


    public function getRoomsPage(User $user, InputBag $query): Page
    {
        $query->set('user', $user->getId());
        return $this->roomService->getPage($query);
    }

    public function getAccountsPage(User $user, InputBag $query): Page
    {
        $query->set('user', $user->getId());
        return $this->accountService->getPage($query);
    }

    public function getReservationsPage(User $user, InputBag $query): Page{
        $query->set('user', $user->getId());
        return $this->reservationService->getPage($query);
    }

    public function getProfileReservationsPage(Account $account, InputBag $query): Page
    {
        return $this->reservationService->getUserReservationsPage($query, $account);
    }
}
