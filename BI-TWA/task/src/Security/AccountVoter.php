<?php

namespace App\Security;

use App\Entity\Account;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Authorization\Voter\Voter;
use Symfony\Component\Security\Core\Security;

class AccountVoter extends Voter {

    const EDIT = 'edit';

    private Security $security;

    public function __construct (Security $security) {
        $this->security = $security;
    }

    protected function supports (string $attribute, $subject) : bool {
        return $attribute == self::EDIT && $subject instanceof Account;
    }

    protected function voteOnAttribute (string $attribute, $subject, TokenInterface $token) : bool {
        $user = $token->getUser();
        if (!$user instanceof Account) return false;

        /** @var Account $post */
        $post = $subject;
        return $attribute == self::EDIT && $this->canEdit($post, $user);
    }

    // User can edit his own account or any account if he is admin
    private function canEdit (Account $post, Account $user) : bool {
        // Edit any account if he is admin
        if ($this->security->isGranted('ROLE_ADMIN')) return true;

        // Edit his own account
        if ($post->hasId() && $user->getId() === $post->getId())
            return true;

        // Edit temporary accounts if he has permanent account
        if ($user->getEmployee()->getId() == $post->getEmployee()->getId() &&
            $user->getExpiration() === null && $post->getExpiration() !== null) return true;

        return false;
    }

}
