<?php

namespace App\Controller;

use App\Entity\Employee;
use App\FormType\EmployeeType;
use App\Service\EmployeeService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController as Controller;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\String\Slugger\SluggerInterface;

class EmployeeController extends Controller {

    private EmployeeService $employeeService;

    public function __construct (EmployeeService $employeeService) {
        $this->employeeService = $employeeService;
    }

    /**
     * @Route("/employee", name="employee_list")
     */
    public function listAction () : Response {
        return $this->render( 'employee/list.html.twig', [
            'employees' => $this->employeeService->getEmployees(),
            'canEdit' => $this->isGranted('edit', new Employee()),
        ]);
    }

    /**
     * @Route("/employee/{username}", name="employee_detail", options={"expose": true})
     */
    public function detailAction (string $username) : Response {
        if (!$this->employeeService->getByUsername($username, $employee))
            throw $this->createNotFoundException("There is no user with username $username");

        return $this->render('employee/detail.html.twig', [
            'user' => $employee,
            'canEdit' => $this->isGranted('edit', $employee)
        ]);
    }

    /**
     * @Route("/employee/{username}/edit", name="employee_edit", options={"expose": true})
     * @Route("/employee/{username}/new", name="employee_create")
     */
    public function editEmployee (Request $request, string $username = null) : Response {
        $create = $request->attributes->get('_route') === "employee_create";
        if ($this->employeeService->getByUsername($username, $employee) === $create)
            throw $create ? new BadRequestHttpException("An user with this username already exists.")
                : new NotFoundHttpException("There is no user with username $username");

        if ($create)
            $employee = (new Employee());

        $this->denyAccessUnlessGranted('edit', $employee);

        $form = $this->createForm(EmployeeType::class, $employee, ['create' => $create])
            ->add($create ? "create" : "edit", SubmitType::class);

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            dump($form->getData());

            /** @var Employee $data */
            $data = $form->getData();
            $this->denyAccessUnlessGranted('edit', $data);

            /** @var UploadedFile $imageFile */
            if ( ($imageFile = $form->get('photoFile')->getData() ?? false) )
                $this->employeeService->setImage($employee, $imageFile);

            $this->employeeService->addEmployee($data);
            return $this->redirectToRoute('employee_list');
        }

        return $this->render('employee/edit.html.twig', [
            'form' => $form->createView(),
            'action' => $create ? "Create new user" : "Edit $username"
        ]);
    }

    /**
     * @Route("/employee/{username}/delete", name="employee_delete", options={"expose":true})
     */
    public function deleteEmployee (string $username) : Response {
        if (!$this->employeeService->getByUsername($username, $employee))
            throw $this->createNotFoundException("There is no user with username $username");

        $this->denyAccessUnlessGranted('edit', $employee);
        $this->employeeService->deleteEmployee($employee);
        return $this->redirectToRoute('employee_list');
    }

}
