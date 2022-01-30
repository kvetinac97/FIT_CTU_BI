<?php

namespace App\Controller;

use App\Entity\Account;
use App\Entity\Reservation;
use App\Entity\Room;
use App\Form\ReservationType;
use App\Service\EntityService\ReservationService;
use App\Service\RelationService\ReservationRelationService;
use DateTime;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("/reservation")
 */
class ReservationController extends AbstractController
{
    private ReservationService $service;
    private ReservationRelationService $relationService;

    public function __construct(ReservationService $service,
                                ReservationRelationService $reservationRelationService)
    {
        $this->service = $service;
        $this->relationService = $reservationRelationService;
    }

    /**
     * @Route("/", name="reservation_index", methods={"GET"})
     */
    public function index(Request $request): Response
    {
        /** @var Account $account */
        $account = $this->getUser();

        // Non-admin can see only his own reservations and reservation he can approve
        $page = $this->isGranted('ROLE_ADMIN') ?
            $this->service->getPage($request->query) :
            $this->service->getNonAdminReservationsPage($request->query, $account);

        return $this->render('reservation/index.html.twig', [
            'reservations' => $page->getData(),
            'current' => $page->getPage(),
            'pages' => $page->getPages(),
            'params' => $page->getUrlParams(),
            'menu' => 'reservation'
        ]);
    }

    /**
     * @Route("/new", name="reservation_new", methods={"GET", "POST"})
     */
    public function new(Request $request, ReservationService $reservationService): Response
    {
        /** @var Account $account */
        $account = $this->getUser();
        $reservation = (new Reservation())->setApproved(false)
            ->setStart(new DateTime())->setUntil(new DateTime())
            ->setCancelled(false)
            ->setAuthor($account->getUser());
        $form = $this->createForm(ReservationType::class, $reservation, [
            'room_selection' => true,
            'room_ids' => $reservationService->getPossibleReservationRooms($account),
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            return $this->redirectToRoute(
                'reservation_new_room',
                ['room' => $form->getData()->getRoom()->getId()],
                Response::HTTP_FOUND
            );
        }

        return $this->renderForm('reservation/new.html.twig', [
            'form' => $form,
            'button_label' => 'Continue',
            'menu' => 'reservation'
        ]);
    }

    /**
     * @Route("/new/{room}", name="reservation_new_room", methods={"GET", "POST"})
     */
    public function newRoom(Request $request, ReservationService $reservationService, Room $room): Response
    {
        $this->denyAccessUnlessGranted('reserve', $room);

        /** @var Account $account */
        $account = $this->getUser();
        $reservation = (new Reservation())
            ->setRoom($room)
            ->setApproved(false)
            ->setCancelled(false)
            ->setAuthor($account->getUser());

        $canApprove = $this->isGranted('approve', $reservation);
        $reservation->setApproved($canApprove);

        $form = $this->createForm(ReservationType::class, $reservation, [
            'room_ids' => [$room],
            'can_approve' => $canApprove,
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $reservationService->create($reservation);
            return $this->redirectToRoute('reservation_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('reservation/new.html.twig', [
            'reservation' => $reservation,
            'form' => $form,
            'menu' => 'reservation'
        ]);
    }

    /**
     * @Route("/{id}", name="reservation_show", methods={"GET"})
     */
    public function show(Reservation $reservation): Response
    {
        $this->denyAccessUnlessGranted('view', $reservation);

        return $this->render('reservation/show.html.twig', [
            'reservation' => $reservation,
            'menu' => 'reservation'
        ]);
    }

    /**
     * @Route("/{id}/users", name="reservation_users", methods={"GET", "POST"})
     */
    public function users(Request $request, Reservation $reservation): Response
    {
        $this->denyAccessUnlessGranted('view', $reservation);

        $userId = $request->get('removeUserId', -1);
        if ($userId !== -1) {
            $this->denyAccessUnlessGranted('approve', $reservation);
            $this->relationService->removeUser($reservation, $userId);
        }

        $page = $this->relationService->getUsersPage($reservation, $request->query);

        return $this->render('reservation/users.html.twig', [
            'reservation' => $reservation,
            'users' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'reservation'
        ]);
    }

    /**
     * @Route("/{id}/addUsers", name="reservation_users_add", methods={"GET", "POST"})
     */
    public function addUsers(Request $request, Reservation $reservation): Response
    {
        $this->denyAccessUnlessGranted('approve', $reservation);
        $userId = $request->get('addUserId', -1);
        if ($userId !== -1){ $this->relationService->addUser($reservation, $userId); }

        $page = $this->relationService->getOtherUsersPage($reservation, $request->query);

        return $this->render('reservation/addUsers.html.twig', [
            'reservation' => $reservation,
            'users' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'reservation'
        ]);
    }

    /**
     * @Route("/{id}/edit", name="reservation_edit", methods={"GET", "POST"})
     */
    public function edit(Request $request, Reservation $reservation): Response
    {
        $this->denyAccessUnlessGranted('approve', $reservation);

        $form = $this->createForm(ReservationType::class, $reservation, [
            'room_ids' => [$reservation->getRoom()->getId()],
            'edit' => true,
            'can_approve' => true
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->service -> edit($reservation);
            return $this->redirectToRoute('reservation_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('reservation/edit.html.twig', [
            'reservation' => $reservation,
            'form' => $form,
            'menu' => 'reservation'
        ]);
    }

    /**
     * @Route("/{id}", name="reservation_delete", methods={"POST"})
     */
    public function delete(Request $request, Reservation $reservation): Response
    {
        $this->denyAccessUnlessGranted('approve', $reservation);
        if ($this->isCsrfTokenValid('delete'.$reservation->getId(), $request->request->get('_token'))) {
            $this->service -> remove($reservation);
        }

        return $this->redirectToRoute('reservation_index', [], Response::HTTP_SEE_OTHER);
    }
}
