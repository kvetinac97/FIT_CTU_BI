<?php

namespace App\Auth;

use App\Entity\Room;
use App\Entity\Account;
use App\Service\EntityService\ReservationService;
use App\Service\EntityService\RoomService;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Authorization\Voter\Voter;
use Symfony\Component\Security\Core\Security;

class RoomVoter extends Voter
{

    const VIEW = 'view';
    const ADMIN = 'admin';
    const RESERVE = 'reserve';

    private Security $security;
    private RoomService $roomService;
    private ReservationService $reservationService;

    public function __construct(Security $security, RoomService $roomService, ReservationService $reservationService)
    {
        $this->security = $security;
        $this->roomService = $roomService;
        $this->reservationService = $reservationService;
    }

    protected function supports(string $attribute, $subject): bool
    {
        switch ($attribute) {
            case self::ADMIN:
            case self::VIEW:
            case self::RESERVE:
                return $subject instanceof Room;
            default:
                return false;
        }
    }

    protected function voteOnAttribute(string $attribute, $subject, TokenInterface $token): bool
    {
        /** @var Room $post */
        $post = $subject;
        switch ($attribute) {
            case self::VIEW:
                return $this->canView($post);
            case self::ADMIN:
                return $this->canAdmin($post, $token->getUser());
            case self::RESERVE:
                return $this->canReserve($post, $token->getUser());
            default:
                return false;
        }
    }

    // User can view room if he is logged in or the room is public
    private function canView(Room $room): bool
    {
        return $this->security->isGranted('ROLE_USER') || $room->getPublic();
    }

    private function canAdmin(Room $room, $account): bool
    {
        return in_array($room->getId(), $this->roomService->getAdminRooms($account));
    }

    private function canReserve(Room $room, $account): bool
    {
        return in_array($room->getId(), $this->reservationService->getPossibleReservationRooms($account));
    }

}
