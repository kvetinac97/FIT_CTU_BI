<?php

namespace App\Controller;

use App\Entity\Account;
use App\Entity\Room;
use App\Form\RoomType;
use App\Service\EntityService\RoomService;
use App\Service\RelationService\RoomRelationService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("/room")
 */
class RoomController extends AbstractController
{
    private RoomService $service;
    private RoomRelationService $relationService;

    public function __construct(RoomService $roomService, RoomRelationService $relationService)
    {
        $this->service = $roomService;
        $this->relationService = $relationService;
    }

    /**
     * @Route("/", name="room_index", methods={"GET"})
     */
    public function index(Request $request): Response
    {
        if (!$this->isGranted('ROLE_USER'))
            $request->query->set('public', true);

        $page = $this->service->getPage($request->query);

        $data = $this->service->dtoArray($page->getData());
        $data = $this->service->addManagerProperty($data);

        return $this->render('room/index.html.twig', [
            'roomsWithManager' => $data,
            'current' => $page->getPage(),
            'pages' => $page->getPages(),
            'params' => $page->getUrlParams(),
            'menu' => 'room'
        ]);
    }

    /**
     * @Route("/{id}/reservations", name="room_reservations", methods={"GET"})
     */
    public function reservations(Request $request, Room $room): Response
    {
        $this->denyAccessUnlessGranted('view', $room);

        if (!$this->isGranted('admin', $room)){
            $request->query->remove('status');
            $request->query->add(["approved" => true]);
        }


        $page = $this->relationService->getReservationsPage($room, $request->query);

        return $this->render('room/reservations.html.twig', [
            'room' => $room,
            'reservations' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'room'
        ]);
    }

    /**
     * @Route("/{id}/users", name="room_users", methods={"GET", "POST"})
     */
    public function users(Request $request, Room $room): Response
    {
        $this->denyAccessUnlessGranted('admin', $room);

        /** @var Account $account */
        $account = $this->getUser();
        $currentUserId = $account->getUser()->getId();

        $removeUserId = $request->get('removeUserId', -1);
        if ($removeUserId !== -1) {
            $this->relationService->removeUser($room, $removeUserId);
            if ($removeUserId == $currentUserId) $this->denyAccessUnlessGranted('ROLE_ADMIN'); // can no longer access
        }

        $changeUserId = $request->get('changeUserId', -1);
        if ($changeUserId !== -1){
            $manager = $request->get('manager', 0);
            $this->relationService->changeUserRole($room, $changeUserId, ($manager === "1"));
            if ($changeUserId == $currentUserId) $this->denyAccessUnlessGranted('ROLE_ADMIN'); // can no longer access
        }


        [$page, $roomUsers] = $this->relationService->getRoomUsersPage($room, $request->query);

        return $this->render('room/users.html.twig', [
            'room' => $room,
            'members' => $roomUsers,
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'room'
        ]);
    }

    /**
     * @Route("/{id}/addUsers", name="room_users_add", methods={"GET", "POST"})
     */
    public function addUsers(Request $request, Room $room): Response
    {
        $this->denyAccessUnlessGranted('admin', $room);

        $userId = $request->get('addUserId', -1);
        if ($userId !== -1){
            $manager = $request->get('manager', 0);
            $this->relationService->addUser($room, $userId, ($manager === "1"));
        }

        $page = $this->relationService->getOtherUsersPage($room, $request->query);

        return $this->render('room/addUsers.html.twig', [
            'room' => $room,
            'users' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'room'
        ]);
    }


    /**
     * @Route("/new", name="room_new", methods={"GET", "POST"})
     */
    public function new(Request $request): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        $room = (new Room())->setOpen(false);
        $form = $this->createForm(RoomType::class, $room);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->service->create($room);

            return $this->redirectToRoute('room_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('room/new.html.twig', [
            'room' => $room,
            'form' => $form,
            'menu' => 'room'
        ]);
    }

    /**
     * @Route("/{id}", name="room_show", methods={"GET"})
     */
    public function show(Room $room): Response
    {
        $this->denyAccessUnlessGranted('view', $room);
        return $this->render('room/show.html.twig', [
            'room' => $room,
            'menu' => 'room'
        ]);
    }

    /**
     * @Route("/{id}/edit", name="room_edit", methods={"GET", "POST"})
     */
    public function edit(Request $request, Room $room): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        $form = $this->createForm(RoomType::class, $room);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->service->edit($room);

            return $this->redirectToRoute('room_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('room/edit.html.twig', [
            'room' => $room,
            'form' => $form,
            'menu' => 'room'
        ]);
    }

    /**
     * @Route("/{id}", name="room_delete", methods={"POST"})
     */
    public function delete(Request $request, Room $room): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        if ($this->isCsrfTokenValid('delete'.$room->getId(), $request->request->get('_token'))) {
            $this->service->remove($room);
        }

        return $this->redirectToRoute('room_index', [], Response::HTTP_SEE_OTHER);
    }
}
