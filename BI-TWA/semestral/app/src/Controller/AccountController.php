<?php

namespace App\Controller;

use App\Entity\Account;
use App\Entity\User;
use App\Form\AccountType;
use App\Service\AbstractService\PageService\AbstractPagingService;
use App\Service\EntityService\AccountService;
use App\Service\EntityService\UserService;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;


class AccountController extends AbstractController
{
    private AccountService $service;

    public function __construct(AccountService $accountService)
    {
        $this->service = $accountService;
    }

    /**
     * @Route("/user/{id}/accounts/new", name="account_new", methods={"GET", "POST"})
     * @Route("/profile/accounts/new", name="account_new_profile", methods={"GET", "POST"})
     */
    public function new(Request $request, UserPasswordHasherInterface $hasher, User $user = null): Response
    {
        $onlyTemporary = false;
        if (!$this->isGranted('create_account')) {
            $this->denyAccessUnlessGranted('create_account_temp');
            $onlyTemporary = true;

            /** @var Account $account */
            $account = $this->getUser();
            $user = $account->getUser();
        }

        $account = new Account();
        $form = $this->createForm(AccountType::class, $account, ['only_temporary' => $onlyTemporary]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $account->setPassword($hasher->hashPassword($account, $account->getPassword()));
            $account->setUser($user);
            $this->service->create($account);

            return $this->redirectToRoute($this->isGranted('create_account') ?
                'user_accounts' : 'profile_accounts', ['id' => $user->getId()], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('account/new.html.twig', [
            'account' => $account,
            'user' => $user,
            'form' => $form,
            'menu' => 'user'
        ]);
    }


    /**
     * @Route("/account/{id}/edit", name="account_edit", methods={"GET", "POST"})
     */
    public function edit(Request $request, Account $account, UserPasswordHasherInterface $hasher): Response
    {
        $this->denyAccessUnlessGranted('edit', $account);
        $form = $this->createForm(AccountType::class, $account, ['only_temporary' => !$this->isGranted('create_account')]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $account->setPassword($hasher->hashPassword($account, $account->getPassword()));
            $this->service->edit($account);

            return $this->redirectToRoute($this->isGranted('create_account') ?
                'user_accounts' : 'profile_accounts', ['id' => $account->getUser()->getId()], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('account/edit.html.twig', [
            'account' => $account,
            'form' => $form,
            'menu' => 'user',
        ]);
    }

    /**
     * @Route("/account/{id}", name="account_delete", methods={"POST"})
     */
    public function delete(Request $request, Account $account): Response
    {
        $this->denyAccessUnlessGranted('edit', $account);
        if ($this->isCsrfTokenValid('delete'.$account->getId(), $request->request->get('_token'))) {
            $this->service->remove($account);
        }

        return $this->redirectToRoute($this->isGranted('create_account') ?
            'user_accounts' : 'profile_accounts', ['id' => $account->getUser()->getId()], Response::HTTP_SEE_OTHER);
    }
}
