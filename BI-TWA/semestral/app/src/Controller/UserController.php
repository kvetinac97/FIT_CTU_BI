<?php

namespace App\Controller;

use App\Entity\Account;
use App\Entity\User;
use App\Form\UserType;
use App\Service\EntityService\UserService;
use App\Service\RelationService\UserRelationService;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("/user")
 */
class UserController extends AbstractController
{
    private UserService $service;
    private UserRelationService $relationService;

    public function __construct(UserService $userService, UserRelationService $userRelationService)
    {
        $this->service = $userService;
        $this->relationService = $userRelationService;
    }


    /**
     * @Route("/", name="user_index", methods={"GET"})
     */
    public function index(Request $request): Response
    {
        $page = $this->service->getPage($request->query);

        return $this->render('user/index.html.twig', [
            'users' => $page->getData(),
            'current' => $page->getPage(),
            'pages' => $page->getPages(),
            'params' => $page->getUrlParams(),
            'menu' => 'user'
        ]);
    }

    /**
     * @Route("/new", name="user_new", methods={"GET", "POST"})
     */
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $user = new User();
        $form = $this->createForm(UserType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $image = $form->get('image')->getData();
            $this->service -> create($user, $image);

            return $this->redirectToRoute('user_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('user/new.html.twig', [
            'user' => $user,
            'form' => $form,
            'menu' => 'user'
        ]);
    }

    /**
     * @Route("/{id}", name="user_show", methods={"GET"})
     */
    public function show(User $user): Response
    {
        return $this->render('user/show.html.twig', [
            'user' => $user,
            'menu' => 'user'
        ]);
    }


    /**
     * @Route("/{id}/rooms", name="user_rooms", methods={"GET"})
     */
    public function rooms(User $user, Request $request): Response
    {
        $page = $this->relationService->getRoomsPage($user, $request->query);

        return $this->render('user/rooms.html.twig', [
            'user' => $user,
            'rooms' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'user'
        ]);
    }

    /**
     * @Route("/{id}/groups", name="user_groups", methods={"GET"})
     */
    public function groups(User $user, Request $request): Response
    {
        $page = $this->relationService->getGroupsPage($user, $request->query);

        return $this->render('user/groups.html.twig', [
            'user' => $user,
            'groups' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'user'
        ]);
    }

    /**
     * @Route("/{id}/reservations", name="user_reservations", methods={"GET"})
     */
    public function reservations(Request $request, User $user): Response
    {
        $page = $this->relationService->getReservationsPage($user, $request->query);

        return $this->render('user/reservations.html.twig', [
            'user' => $user,
            'reservations' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'user'
        ]);
    }


    /**
     * @Route("/{id}/accounts", name="user_accounts", methods={"GET"})
     */
    public function accounts(Request $request, User $user): Response
    {
        $page = $this->relationService->getAccountsPage($user, $request->query);

        return $this->render('user/accounts.html.twig', [
            'user' => $user,
            'accounts' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'user'
        ]);
    }


    /**
     * @Route("/{id}/edit", name="user_edit", methods={"GET", "POST"})
     */
    public function edit(Request $request, User $user): Response
    {
        $form = $this->createForm(UserType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $image = $form->get('image')->getData();
            $this->service -> edit($user, $image);

            return $this->redirectToRoute('user_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('user/edit.html.twig', [
            'user' => $user,
            'form' => $form,
            'menu' => 'user'
        ]);
    }

    /**
     * @Route("/{id}", name="user_delete", methods={"POST"})
     */
    public function delete(Request $request, User $user): Response
    {
        if ($this->isCsrfTokenValid('delete' . $user->getId(), $request->request->get('_token'))) {
            $this->service->remove($user);
        }

        return $this->redirectToRoute('user_index', [], Response::HTTP_SEE_OTHER);
    }
}
