<?php

namespace App\Service;

use App\Entity\Employee;
use App\Repository\EmployeeRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\File\UploadedFile;

class EmployeeService {

    private const IMAGE_PATH = __DIR__ . '/../../public/employees/';

    private EmployeeRepository $employeeRepository;
    private EntityManagerInterface $entityManager;

    public function __construct ( EmployeeRepository $employeeRepository,
                                  EntityManagerInterface $entityManager ) {
        $this->employeeRepository = $employeeRepository;
        $this->entityManager = $entityManager;
    }

    public function getByUsername (?string $username, &$employee) : bool {
        if ( ($e = $this->employeeRepository->getByUsername($username)) !== NULL ) {
            $employee = $e;
            return true;
        }
        return false;
    }

    /** @return Employee[] */
    public function getEmployees (?string $name = null) : array {
        return $name === null ? $this->employeeRepository->findAll()
            : $this->employeeRepository->findByName($name);
    }

    public function addEmployee (Employee $employee) {
        $this->entityManager->persist($employee);
        $this->entityManager->flush();
    }

    public function deleteEmployee (Employee $employee) {
        $photo = $employee->getPhoto();
        $this->entityManager->remove($employee);
        $this->entityManager->flush();

        if ($photo !== NULL)
            unlink(self::IMAGE_PATH . $photo);
    }

    public function setImage (Employee $employee, UploadedFile $imageFile) {
        $newFilename = uniqid() . '.' . $imageFile->guessExtension();
        try {
            $imageFile->move(self::IMAGE_PATH, $newFilename);
            dump("Moving to " . self::IMAGE_PATH . $newFilename);
            if ($employee->getPhoto() !== NULL)
                unlink(self::IMAGE_PATH . $employee->getPhoto());
        }
        catch (FileException $exc) {
            dump($exc);
        }

        $employee->setPhoto($newFilename);
    }

}
