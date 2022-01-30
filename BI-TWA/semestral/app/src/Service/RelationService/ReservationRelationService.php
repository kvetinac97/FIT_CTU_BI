<?php

namespace App\Service\RelationService;

use App\Entity\Reservation;
use App\Service\AbstractService\PageService\Page;
use App\Service\EntityService\UserService;
use Symfony\Component\HttpFoundation\InputBag;

class ReservationRelationService
{
    private UserService $userService;

    public function __construct(UserService $userService)
    {
        $this->userService = $userService;
    }

    public function getUsersPage(Reservation $reservation, InputBag $query): Page
    {
        $query->set('reservation', $reservation->getId());
        return $this->userService->getPage($query);
    }

    public function getOtherUsersPage(Reservation $reservation, InputBag $query): Page
    {
        $query->remove('reservation');
        return $this->userService->getWithoutReservationPage($query, $reservation);
    }

    public function addUser(Reservation $reservation, int $userId)
    {
        if (($user = $this->userService->find($userId)) !== null){
            $reservation->addUser($user);
            $this->userService->flush();
        }
    }

    public function removeUser(Reservation $reservation, int $userId)
    {
        if (($user = $this->userService->find($userId)) !== null){
            $reservation->removeUser($user);
            $this->userService->flush();
        }
    }
}