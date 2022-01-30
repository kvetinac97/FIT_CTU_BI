<?php

namespace App\Controller;

use App\Entity\Account;
use App\FormType\AccountType;
use App\Service\AccountService;
use App\Service\EmployeeService;
use DateTime;
use Doctrine\DBAL\Exception\UniqueConstraintViolationException;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController as Controller;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;

class AccountController extends Controller {

    private AccountService $accountService;
    private EmployeeService $employeeService;

    public function __construct (AccountService $accountService, EmployeeService $employeeService) {
        $this->accountService = $accountService;
        $this->employeeService = $employeeService;
    }

    // Help Form creation method
    private function createAccountForm (Request $request, ?UserPasswordHasherInterface $hash, ?Account $account, &$form) : ?Response {
        $form = $this->createForm(AccountType::class, $account, ['create' => !$account->hasId()])
            ->add($account->hasId() ? "edit" : "create", SubmitType::class);
        $this->denyAccessUnlessGranted('edit', $account);

        $form->handleRequest($request);
        if (!$form->isSubmitted() || !$form->isValid()) return null;

        /** @var Account $account */
        $account = $form->getData();
        if ( ($plain = $form->get('plain_password')->getData()) !== null )
            $account->setPassword($hash->hashPassword($account, $plain));

        $this->denyAccessUnlessGranted('edit', $account);
        try {
            $this->accountService->addAccount($account);
        }
        catch (UniqueConstraintViolationException) {
            throw new BadRequestHttpException("An account with this name already exists.");
        }

        return $this->redirectToRoute('account_list', ['username' => $account->getEmployee()->getUsername()]);
    }

    /**
     * @Route("/employee/{username}/account", name="account_list")
     */
    public function listAccounts (Request $request, string $username) : Response {
        if (!$this->employeeService->getByUsername($username, $employee))
            throw new NotFoundHttpException("There is no user with username $username");
        $this->denyAccessUnlessGranted('edit', $employee);

        $account = (new Account())->setEmployee($employee)->setExpiration(new DateTime());
        if ($this->isGranted('edit', $account))
            $this->createAccountForm($request, null, $account, $form);

        return $this->render( 'account/list.html.twig', [
            'accounts' => $this->accountService->getAccounts($employee),
            'form' => isset($form) ? $form->createView() : false,
            'username' => $username
        ]);
    }

    /**
     * @Route("/employee/{username}/account/{name}/edit", name="account_edit", options={"expose"=true})
     * @Route("/employee/{username}/account/new", name="account_create")
     */
    public function editAction (Request $request, UserPasswordHasherInterface $hash,
                                string $username, string $name = NULL) : Response {
        if (!$this->employeeService->getByUsername($username, $employee))
            throw new NotFoundHttpException("There is no user with username $username");

        $create = $request->attributes->get('_route') === "account_create";

        /** @var Account $account */
        if ($create)
            $account = (new Account())->setEmployee($employee)->setExpiration(new DateTime());
        elseif (!$this->accountService->getAccount($employee, $name, $account))
            throw new NotFoundHttpException("There is no account with name $name");

        $response = $this->createAccountForm($request, $hash, $account, $form);
        return $response ?? $this->render('account/edit.html.twig', [
            'form' => $form->createView(),
            'action' => $create ? "Create new account" : "Edit $name"
        ]);
    }

    /**
     * @Route("/employee/{username}/account/{name}/delete", name="account_delete", options={"expose"=true})
     */
    public function deleteEmployee (string $username, string $name) : Response {
        if (!$this->employeeService->getByUsername($username, $employee))
            throw $this->createNotFoundException("There is no user with username $username");

        /** @var Account $account */
        if (!$this->accountService->getAccount($employee, $name, $account))
            throw $this->createNotFoundException("There is no account with name $name");

        $this->accountService->deleteAccount($account);
        return $this->redirectToRoute('account_list', ['username' => $username]);
    }

}
