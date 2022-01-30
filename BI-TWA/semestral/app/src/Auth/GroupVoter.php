<?php

namespace App\Auth;

use App\Entity\Account;
use App\Entity\Group;
use App\Service\EntityService\GroupService;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Authorization\Voter\Voter;
use Symfony\Component\Security\Core\Security;

class GroupVoter extends Voter
{

    const VIEW_GROUPS = 'view_groups';
    const VIEW = 'view';

    private Security $security;
    private GroupService $groupService;

    public function __construct(Security $security, GroupService $groupService)
    {
        $this->security = $security;
        $this->groupService = $groupService;
    }

    protected function supports(string $attribute, $subject): bool
    {
        switch ($attribute) {
            case self::VIEW_GROUPS:
                return $subject === null;
            case self::VIEW:
                return $subject instanceof Group;
            default:
                return false;
        }
    }

    protected function voteOnAttribute(string $attribute, $subject, TokenInterface $token): bool
    {
        /** @var Account $account */
        $account = $token->getUser();

        /** @var Group $post */
        $post = $subject;
        switch ($attribute) {
            case self::VIEW_GROUPS:
                return $this->canViewGroups($account);
            case self::VIEW:
                return $this->canView($account, $post);
            default:
                return false;
        }
    }
    
    private function canViewGroups($account): bool
    {
        return !empty($this->groupService->getAdminGroups($account));
    }
    
    private function canView(Account $account, Group $group): bool
    {
        return in_array($group->getId(), $this->groupService->getAdminGroups($account));
    }

}
