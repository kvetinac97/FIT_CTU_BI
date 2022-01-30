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
 * @Route("/profile")
 */
class ProfileController extends AbstractController
{
    private UserService $service;
    private UserRelationService $relationService;

    public function __construct(UserService $userService, UserRelationService $userRelationService)
    {
        $this->service = $userService;
        $this->relationService = $userRelationService;

    }

    /**
     * @Route("/", name="profile_show", methods={"GET"})
     */
    public function show(): Response
    {
        /** @var Account $account */
        $account = $this->getUser();

        return $this->render('profile/show.html.twig', [
            'user' => $account->getUser(),
            'menu' => 'profile'
        ]);
    }


    /**
     * @Route("/rooms", name="profile_rooms", methods={"GET"})
     */
    public function rooms(Request $request): Response
    {
        /** @var Account $account */
        $account = $this->getUser();
        $user = $account->getUser();

        $page = $this->relationService->getRoomsPage($user, $request->query);

        return $this->render('profile/rooms.html.twig', [
            'user' => $user,
            'rooms' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'profile'
        ]);
    }

    /**
     * @Route("/groups", name="profile_groups", methods={"GET"})
     */
    public function groups(Request $request): Response
    {
        /** @var Account $account */
        $account = $this->getUser();
        $user = $account->getUser();

        $page = $this->relationService->getGroupsPage($user, $request->query);

        return $this->render('profile/groups.html.twig', [
            'user' => $user,
            'groups' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'profile'
        ]);
    }

    /**
     * @Route("/reservations", name="profile_reservations", methods={"GET"})
     */
    public function reservations(Request $request): Response
    {
        /** @var Account $account */
        $account = $this->getUser();
        $user = $account->getUser();

        $page = $this->relationService->getProfileReservationsPage($account, $request->query);

        return $this->render('profile/reservations.html.twig', [
            'user' => $user,
            'reservations' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'profile'
        ]);
    }


    /**
     * @Route("/accounts", name="profile_accounts", methods={"GET"})
     */
    public function accounts(Request $request): Response
    {
        /** @var Account $account */
        $account = $this->getUser();
        $user = $account->getUser();

        $page = $this->relationService->getAccountsPage($user, $request->query);

        return $this->render('profile/accounts.html.twig', [
            'user' => $user,
            'accounts' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'profile'
        ]);
    }


    /**
     * @Route("/edit", name="profile_edit", methods={"GET", "POST"})
     */
    public function edit(Request $request): Response
    {
        /** @var Account $account */
        $account = $this->getUser();
        $user = $account->getUser();

        $form = $this->createForm(UserType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $image = $form->get('image')->getData();
            $this->service -> edit($user, $image);

            return $this->redirectToRoute('profile_show', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('profile/edit.html.twig', [
            'user' => $user,
            'form' => $form,
            'menu' => 'profile'
        ]);
    }

}
