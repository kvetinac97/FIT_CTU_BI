<?php

namespace App\Auth;

use App\Entity\Account;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Authorization\Voter\Voter;
use Symfony\Component\Security\Core\Security;

class AccountVoter extends Voter
{

    const CREATE = 'create_account';
    const CREATE_TEMPORARY = 'create_account_temp';
    const EDIT = 'edit';

    private Security $security;

    public function __construct(Security $security)
    {
        $this->security = $security;
    }

    protected function supports(string $attribute, $subject): bool
    {
        switch ($attribute) {
            case self::EDIT:
                return $subject instanceof Account;
            case self::CREATE:
            case self::CREATE_TEMPORARY:
                return $subject === null;
            default:
                return false;
        }
    }

    protected function voteOnAttribute(string $attribute, $subject, TokenInterface $token): bool
    {
        $user = $token->getUser();
        if (!$user instanceof Account) return false;

        /** @var Account $post */
        $post = $subject;
        switch ($attribute) {
            case self::EDIT:
                return $this->canEdit($post, $user);
            case self::CREATE:
            case self::CREATE_TEMPORARY:
                return $this->canCreate($user, $attribute === self::CREATE_TEMPORARY);
            default:
                return false;
        }
    }

    // User can edit his own temporary accounts from permanent account or any account if he is admin
    private function canEdit(Account $post, Account $account): bool
    {
        // Edit any account if he is admin
        if ($this->security->isGranted('ROLE_ADMIN')) return true;

        // Edit his temporary accounts if he is logged in from permanent account
        if ($account->getUser()->getId() == $post->getUser()->getId() &&
            $account->getExpiration() === null && $post->getExpiration() !== null) return true;

        return false;
    }

    private function canCreate(Account $account, bool $temporary): bool
    {
        // User can create temporary accounts if he is logged in from permanent account
        if ($account->getExpiration() === null && $temporary)
            return true;
        return $this->security->isGranted('ROLE_ADMIN');
    }

}
