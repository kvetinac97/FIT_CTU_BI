<?php

namespace App\Controller;

use App\Entity\Account;
use App\Entity\Group;
use App\Form\GroupType;
use App\Service\EntityService\GroupService;
use App\Service\RelationService\GroupRelationService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("/group")
 */
class GroupController extends AbstractController
{

    private GroupService $service;
    private GroupRelationService $relationService;

    public function __construct (GroupService $groupService, GroupRelationService $groupRelationService) {
        $this->service = $groupService;
        $this->relationService = $groupRelationService;
    }

    /**
     * @Route("/", name="group_index", methods={"GET"})
     */
    public function index(Request $request): Response
    {
        $this->denyAccessUnlessGranted('view_groups');
        $page = $this->isGranted('ROLE_ADMIN') ? $this->service->getPage($request->query)
            : $this->service->getNonAdminGroupsPage($request->query, $this->getUser());

        $data = $this->service->addManagerProperty($page->getData());

        return $this->render('group/index.html.twig', [
            'groupsWithManager' => $data,
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'group'
        ]);
    }

    /**
     * @Route("/new", name="group_new", methods={"GET", "POST"})
     */
    public function new(Request $request): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        $group = new Group();
        $form = $this->createForm(GroupType::class, $group);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->service->create($group);
            return $this->redirectToRoute('group_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('group/new.html.twig', [
            'group' => $group,
            'form' => $form,
            'menu' => 'group'
        ]);
    }

    /**
     * @Route("/{id}", name="group_show", methods={"GET"})
     */
    public function show(Group $group): Response
    {
        $this->denyAccessUnlessGranted('view', $group);
        return $this->render('group/show.html.twig', [
            'group' => $group,
            'menu' => 'group'
        ]);
    }

    /**
     * @Route("/{id}/users", name="group_users", methods={"GET", "POST"})
     */
    public function users(Request $request, Group $group): Response
    {
        $this->denyAccessUnlessGranted('view', $group);

        /** @var Account $account */
        $account = $this->getUser();
        $currentUserId = $account->getUser()->getId();

        $removeUserId = $request->get('removeUserId', -1);
        if ($removeUserId !== -1) {
            $this->relationService->removeUser($group, $removeUserId);
            if ($removeUserId == $currentUserId) $this->denyAccessUnlessGranted('ROLE_ADMIN'); // can no longer access
        }

        $changeUserId = $request->get('changeUserId', -1);
        if ($changeUserId !== -1){
            $manager = $request->get('manager', 0);
            $this->relationService->changeUserRole($group, $changeUserId, ($manager === "1"));
            if ($changeUserId == $currentUserId) $this->denyAccessUnlessGranted('ROLE_ADMIN'); // can no longer access
        }

        [$page, $groupMembers] = $this->relationService->getGroupMembersPage($group, $request->query);

        return $this->render('group/users.html.twig', [
            'group' => $group,
            'members' => $groupMembers,
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'group'
        ]);
    }

    /**
     * @Route("/{id}/addUsers", name="group_users_add", methods={"GET", "POST"})
     */
    public function addUsers(Request $request, Group $group): Response
    {
        $this->denyAccessUnlessGranted('view', $group);

        $userId = $request->get('addUserId', -1);
        if ($userId !== -1){
            $manager = $request->get('manager', 0);
            $this->relationService->addUser($group, $userId, ($manager === "1"));
        }

        $page = $this->relationService->getOtherUsersPage($group, $request->query);

        return $this->render('group/addUsers.html.twig', [
            'group' => $group,
            'users' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'group'
        ]);
    }

    /**
     * @Route("/{id}/rooms", name="group_rooms", methods={"GET", "POST"})
     */
    public function rooms(Request $request, Group $group): Response
    {
        $this->denyAccessUnlessGranted('view', $group);
        $roomId = $request->get('removeRoomId', -1);
        if ($roomId !== -1) {
            $this->denyAccessUnlessGranted('ROLE_ADMIN');
            $this->relationService->removeRoom($group, $roomId);
        }

        $page = $this->relationService->getRoomsPage($group, $request->query);


        return $this->render('group/rooms.html.twig', [
            'group' => $group,
            'rooms' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'group'
        ]);
    }

    /**
     * @Route("/{id}/addRooms", name="group_rooms_add", methods={"GET", "POST"})
     */
    public function addRooms(Request $request, Group $group): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        $roomId = $request->get('addRoomId', -1);
        if ($roomId !== -1){ $this->relationService->addRoom($group, $roomId); }

        $page = $this->relationService->getOtherRoomsPage($group, $request->query);


        return $this->render('group/addRooms.html.twig', [
            'group' => $group,
            'rooms' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'group'
        ]);
    }

    /**
     * @Route("/{id}/edit", name="group_edit", methods={"GET", "POST"})
     */
    public function edit(Request $request, Group $group): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        $form = $this->createForm(GroupType::class, $group, ['group_id' => $group->getId()]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->service->edit($group);
            return $this->redirectToRoute('group_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('group/edit.html.twig', [
            'group' => $group,
            'form' => $form,
            'menu' => 'group'
        ]);
    }

    /**
     * @Route("/{id}", name="group_delete", methods={"POST"})
     */
    public function delete(Request $request, Group $group): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        if ($this->isCsrfTokenValid('delete'.$group->getId(), $request->request->get('_token'))) {
            $this->service->remove($group);
        }

        return $this->redirectToRoute('group_index', [], Response::HTTP_SEE_OTHER);
    }
}
