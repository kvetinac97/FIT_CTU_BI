<?php

namespace App\RestController;

use App\Entity\Account;
use App\FormType\AccountType;
use App\Service\AccountService;
use App\Service\EmployeeService;
use Doctrine\DBAL\Exception\NotNullConstraintViolationException;
use Doctrine\DBAL\Exception\UniqueConstraintViolationException;
use FOS\RestBundle\Controller\AbstractFOSRestController as RestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

/**
 * @Rest\View(serializerEnableMaxDepthChecks=true)
 */
class AccountRestController extends RestController {

    private AccountService $accountService;
    private EmployeeService $employeeService;

    public function __construct (AccountService $accountService, EmployeeService $employeeService) {
        $this->accountService = $accountService;
        $this->employeeService = $employeeService;
    }

    /**
     * @Rest\Get("/employee/{username}/account", name = "rest_account_list")
     */
    public function listAccounts (string $username) : Response {
        if (!$this->employeeService->getByUsername($username, $employee))
            return $this->handleView($this->view(null, Response::HTTP_NOT_FOUND));

        $data = $this->accountService->getAccounts($employee);
        $data = array_map(fn(Account $account) => $account->setCanEdit($this->isGranted('edit', $account)), $data);
        return $this->handleView($this->view($data, Response::HTTP_OK));
    }

    /**
     * @Rest\Post("/employee/{username}/account", name = "account_create_rest")
     * @Rest\Put("/employee/{username}/account/{accName}", name = "account_edit_rest")
     */
    public function createAccount (Request $request, UserPasswordHasherInterface $hash, string $username, string $accName = null) : Response {
        if (!$this->employeeService->getByUsername($username, $employee))
            return $this->handleView($this->view(null, Response::HTTP_NOT_FOUND));

        if ($accName !== null) {
            if (!$this->accountService->getAccount($employee, $accName, $account))
                return $this->handleView($this->view(null, Response::HTTP_NOT_FOUND));
        }
        else $account = (new Account())->setEmployee($employee);

        $form = $this->createForm(AccountType::class, $account, ['csrf_protection' => false,
            'allow_extra_fields' => true, 'restEdit' => $accName !== null, 'create' => true]);
        $form->handleRequest($request);

        if (!$form->isSubmitted() || !$form->isValid())
            return $this->handleView($this->view($form, Response::HTTP_BAD_REQUEST));

        /** @var Account $data */
        $data = $form->getData();
        if ( ($plain = $form->get('plain_password')->getData()) !== null )
            $account->setPassword($hash->hashPassword($account, $plain));

        try {
            $this->accountService->addAccount($data);
        }
        catch (NotNullConstraintViolationException|UniqueConstraintViolationException $exception) {
            return $this->handleView($this->view(null, Response::HTTP_BAD_REQUEST));
        }

        return $this->handleView(
            $this->view(null, $accName === null ? Response::HTTP_CREATED : Response::HTTP_NO_CONTENT)
        );
    }

    /**
     * @Rest\Delete("/employee/{username}/account/{name}", name="account_delete_rest")
     */
    public function deleteAccount (string $username, string $name) : Response {
        if (!$this->employeeService->getByUsername($username, $employee)
            || !$this->accountService->getAccount($employee, $name, $account))
            return $this->handleView($this->view(null, Response::HTTP_NOT_FOUND));

        $this->accountService->deleteAccount($account);
        return $this->handleView($this->view(null, Response::HTTP_NO_CONTENT));
    }

}
