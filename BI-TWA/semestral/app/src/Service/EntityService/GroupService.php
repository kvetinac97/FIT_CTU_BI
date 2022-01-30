<?php

namespace App\Service\EntityService;

use App\Entity\Account;
use App\Entity\Group;
use App\Entity\Member;
use App\Entity\User;
use App\Repository\GroupRepository;
use App\Repository\MemberRepository;
use App\Service\AbstractService\PageService\AbstractPagingService;
use App\Service\AbstractService\PageService\Page;
use App\Service\AbstractService\PageService\PagingQuery;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\ORM\Tools\Pagination\Paginator;
use Symfony\Component\HttpFoundation\InputBag;
use Exception;


class GroupService extends AbstractPagingService
{
    private EntityManagerInterface $em;
    private MemberRepository $memberRepository;


    public function __construct(EntityManagerInterface $entityManager,
                                GroupRepository $repository,
                                MemberRepository $memberRepository)
    {
        parent::__construct($repository);
        $this->em = $entityManager;
        $this->memberRepository = $memberRepository;
    }

    public function find(?string $id): ?Group
    {
        return $this->repository->find($id);
    }

    public function create(Group $group)
    {
        $this->em->persist($group);
        $this->flush();
    }


    public function edit(Group $group)
    {
        $this->flush();
    }

    public function remove(Group $group)
    {
        // Clear parent group on each child
        /** @var Group[] $children child groups */
        foreach ($this->repository->findBy(['parent_group' => $group]) as $child)
            $this->em->persist($child->setParentGroup(null));

        $this->em->remove($group);
        $this->flush();
    }

    public function flush()
    {
        try {
            $this->em->flush();
        }
        catch(\Doctrine\DBAL\Exception | \Exception $e){
            $errorMessage = $e->getMessage();
            dd($errorMessage);
        }
    }

    /** @return int[] */
    public function getAdminGroups ($account) : array {
        if (!$account instanceof Account) return [];

        // Admin can do anything
        if (in_array('ROLE_ADMIN', $account->getRoles()))
            return array_map(fn(Group $group) => $group->getId(), $this->repository->findAll());

        // User must be member - manager to admin a group
        $user = $account->getUser();
        return $user->getGroupMemberships()
            ->filter(fn(Member $member) => $member->getManager())
            ->map(fn(Member $member) => $member->getGroup()->getId())->toArray();
    }

    public function getNonAdminGroupsPage(InputBag $query, $account): Page
    {
        $pagingQuery = new PagingQuery($this->repository);

        // Group Manager
        $adminGroups = $this->getAdminGroups($account);
        $pagingQuery->orSimpleSearchMany(['id' => $adminGroups]);

        return $this->getPage($query, $pagingQuery);
    }

    /**
     * Adds users to group
     * if we're trying to add user which is already in the group, nothing happens
     * @param Group $group
     * @param User[] $users
     */
    public function addUsers(Group $group, array $users)
    {
        $members = array_map(fn(User $user) => (new Member())->setGroup($group)->setUser($user)->setManager(false), $users);
        foreach ($members as $member) {
            try {
                $this->em->persist($member);
            } catch (Exception $exception) {
                dump($exception);
            }
        }
        try {
            $this->em->flush();
        } catch (Exception $exception) {
            dump($exception);
        }
    }

    /**
     * Removes users from group
     * if we're trying to remove user which is not in the group, nothing happens
     * @param Group $group
     * @param User[] $users
     */
    public function removeUsers(Group $group, array $users)
    {
        $members = array_filter(array_map(fn(User $user) => $this->memberRepository->findOneBy(['user' => $user, 'group' => $group]), $users));

        foreach ($members as $member) {
            try {
                $this->em->remove($member);
            } catch (Exception $exception) {
                dump($exception);
            }
        }
        try {
            $this->em->flush();
        } catch (Exception $exception) {
            dump($exception);
        }
    }

    public function getMembersFromUsers(Group $group, Paginator $users): array
    {
        return $this->memberRepository->findBy(['group' => $group, 'user' => $users->getIterator()->getArrayCopy()]);
    }

    public function changeGroupMemberRole(Group $group, ?User $user, bool $manager)
    {
        $member = $this->memberRepository->findOneBy(['user' => $user, 'group' => $group]);
        if ($member !== null && $member->getManager() !== $manager){
            $member->setManager($manager);
            $this->flush();
        }
    }

    public function addMember(Group $group, ?User $user, bool $manager)
    {
        $member = $this->memberRepository->findOneBy(['user' => $user, 'group' => $group]);
        if ($member === null){
            $member = new Member();
            $member->setGroup($group);
            $member->setUser($user);
            $member->setManager($manager);
            $this->em->persist($member);
            $this->flush();
        }
    }

    public function addManagerProperty(Paginator $groups): array
    {
        $roomsWithManagerProperty = [];
        foreach($groups as $group){
            $roomsWithManagerProperty[] = [
                'group' => $group,
                'manager' => $this->memberRepository->findOneBy(['group' => $group, 'manager' => true])
            ];
        }
        return $roomsWithManagerProperty;
    }

    /*------------ OVERRIDE ABSTRACT METHODS ------------------*/
    protected function getOrderFields(): array
    {
        return ['name'];
    }

    protected function getJoinCriteria(InputBag $query): array
    {
        $criteria = [];

        $room = $query->getInt('room', -1);
        if (($room !== -1) && property_exists(Group::class, 'rooms'))
        {
            $criteria['rooms'] = $room;
        }

        return $criteria;
    }

    protected function getCriteria(InputBag $query): array
    {
        $criteria = [];

        if (($name = $query->get('name')) && property_exists(Group::class, 'name')){
            $criteria['name'] = $name;
        }

        return $criteria;
    }

    protected function getDecompositionJoinCriteria(InputBag $query): array
    {
        $criteria = [];

        $member = $query->getInt('user', -1);
        if (($member !== -1) && property_exists(Group::class, 'members'))
        {
            $criteria['user'] = ['members', $member];
        }

        return $criteria;
    }
}
