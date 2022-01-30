<?php

namespace App\Auth;

use App\Entity\Group;
use App\Entity\Member;
use App\Entity\Reservation;
use App\Entity\User;
use App\Entity\Account;
use App\Repository\RoomRepository;
use App\Service\EntityService\ReservationService;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Authorization\Voter\Voter;
use Symfony\Component\Security\Core\Security;

class ReservationVoter extends Voter
{

    const CREATE = 'create';
    const APPROVE = 'approve';
    const VIEW = 'view';

    private Security $security;
    private ReservationService $reservationService;

    public function __construct(Security $security, ReservationService $reservationService)
    {
        $this->security = $security;
        $this->reservationService = $reservationService;
    }

    protected function supports(string $attribute, $subject): bool
    {
        switch ($attribute) {
            case self::APPROVE:
            case self::VIEW:
                return $subject instanceof Reservation;
            case self::CREATE:
                return $subject === null;
            default:
                return false;
        }
    }

    protected function voteOnAttribute(string $attribute, $subject, TokenInterface $token): bool
    {
        $user = $token->getUser();
        if (!$user instanceof Account) return false;

        /** @var Reservation $post */
        $post = $subject;
        switch ($attribute) {
            case self::CREATE:
                return $this->canCreate($user);
            case self::VIEW:
                return $this->canView($post, $user);
            case self::APPROVE:
                return $this->canApprove($post, $user);
            default:
                return false;
        }
    }

    // User can approve reservation if he is the room manager, admin of group which owns a room or admin
    private function canApprove(Reservation $reservation, Account $account): bool
    {
        return in_array($reservation->getRoom()->getId(), $this->reservationService->getPossibleAdminRooms($account));
    }

    // User can view reservation if he can approve it or if he created it or if he is member of it
    private function canView(Reservation $reservation, Account $account): bool
    {
        $user = $account->getUser();
        return $this->canApprove($reservation, $account) || $reservation->getAuthor()->getId() == $user->getId() ||
            $reservation->getUsers()->contains($user);
    }

    // User can create reservation if he is a room user, member of group which owns a room or admin
    private function canCreate(Account $account): bool
    {
        return !empty($this->reservationService->getPossibleReservationRooms($account));
    }

}
