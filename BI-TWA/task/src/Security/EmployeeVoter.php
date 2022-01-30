<?php

namespace App\Security;

use App\Entity\Employee;
use App\Entity\Account;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Authorization\Voter\Voter;
use Symfony\Component\Security\Core\Security;

class EmployeeVoter extends Voter {

    const EDIT = 'edit';

    private Security $security;

    public function __construct (Security $security) {
        $this->security = $security;
    }

    protected function supports (string $attribute, $subject) : bool {
        return $attribute == self::EDIT && $subject instanceof Employee;
    }

    protected function voteOnAttribute (string $attribute, $subject, TokenInterface $token) : bool {
        $user = $token->getUser();
        if (!$user instanceof Account) return false;

        /** @var Employee $post */
        $post = $subject;
        return $attribute == self::EDIT && $this->canEdit($post, $user);
    }

    // User can edit himself or any account if he is admin
    private function canEdit (Employee $post, Account $user) : bool {
        // Edit any account if he is admin
        if ($this->security->isGranted('ROLE_ADMIN')) return true;

        // Edit his own account
        if ($post->hasId() && $user->getEmployee()->getId() === $post->getId()) return true;

        return false;
    }

}
