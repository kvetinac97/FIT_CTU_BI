<?php

namespace App\RestController;

use App\Entity\Employee;
use App\FormType\EmployeeType;
use App\Service\EmployeeService;
use Doctrine\DBAL\Exception\NotNullConstraintViolationException;
use Doctrine\DBAL\Exception\UniqueConstraintViolationException;
use FOS\RestBundle\Controller\AbstractFOSRestController as RestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Request\ParamFetcherInterface;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

/**
 * @Rest\View(serializerEnableMaxDepthChecks=true)
 */
class EmployeeRestController extends RestController {

    private EmployeeService $employeeService;

    public function __construct (EmployeeService $employeeService) {
        $this->employeeService = $employeeService;
    }

    /**
     * @Rest\Get("/employee", name = "employee_list_rest")
     * @Rest\QueryParam(name = "name", nullable=true, default=null)
     */
    public function listAction (ParamFetcherInterface $paramFetcher) : Response {
        $data = $this->employeeService->getEmployees($paramFetcher->get('name'));
        $data = array_map(fn(Employee $employee) =>
            $employee
                ->setCanEdit($this->isGranted('edit', $employee))
                ->setCanView($this->isGranted('ROLE_USER', $employee))
        , $data);
        return $this->handleView($this->view($data, Response::HTTP_OK));
    }

    /**
     * @Rest\Get("/employee/{username}", name="employee_detail_rest")
     */
    public function detailAction (string $username) : Response {
        if (!$this->employeeService->getByUsername($username, $employee))
            return $this->handleView($this->view(null, Response::HTTP_NOT_FOUND));
        return $this->handleView($this->view($employee, Response::HTTP_OK));
    }

    /**
     * @Rest\Post("/employee", name="employee_create_rest")
     * @Rest\Put("/employee/{username}", name="employee_edit_rest")
     *
     * @Rest\RequestParam(name = "name")
     * @Rest\RequestParam(name = "username", nullable=true)
     * @Rest\RequestParam(name = "phone")
     * @Rest\RequestParam(name = "email")
     * @Rest\RequestParam(name = "website")
     * @Rest\RequestParam(name = "description")
     * @Rest\FileParam(name = "photoFile", nullable=true)
     */
    public function createEmployee (Request $request, ParamFetcherInterface $paramFetcher, string $username = null) : Response {
        if ($username !== null) {
            if (!$this->employeeService->getByUsername($username, $employee))
                return $this->handleView($this->view(null, Response::HTTP_NOT_FOUND));
        }
        else $employee = new Employee();

        $form = $this->createForm(EmployeeType::class, $employee, ['create' => $username === null, 'csrf_protection' => false])
            ->submit($paramFetcher->all());

        $form->handleRequest($request);

        if (!$form->isSubmitted() || !$form->isValid())
            return $this->handleView($this->view($form, Response::HTTP_BAD_REQUEST));

        dump($form->getData());

        /** @var Employee $data */
        $data = $form->getData();

        /** @var UploadedFile $imageFile */
        if ( ($imageFile = $form->get('photoFile')->getData() ?? false) )
            $this->employeeService->setImage($employee, $imageFile);

        try {
            $this->employeeService->addEmployee($data);
        }
        catch (UniqueConstraintViolationException|NotNullConstraintViolationException $exception) {
            return $this->handleView($this->view(null, Response::HTTP_BAD_REQUEST));
        }

        return $this->handleView(
            $this->view(null, $username === null ? Response::HTTP_CREATED : Response::HTTP_NO_CONTENT)
        );
    }

    /**
     * @Rest\Delete("/employee/{username}", name="employee_delete_rest")
     */
    public function deleteEmployee (string $username) : Response {
        if (!$this->employeeService->getByUsername($username, $employee))
            return $this->handleView($this->view(null, Response::HTTP_NOT_FOUND));

        $this->employeeService->deleteEmployee($employee);
        return $this->handleView($this->view(null, Response::HTTP_NO_CONTENT));
    }

}
