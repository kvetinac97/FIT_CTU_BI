<?php

namespace kvetinac97;

use Symfony\Component\Validator\ConstraintViolationList;
use Symfony\Component\Validator\Validator\ValidatorInterface;
use Symfony\Component\Validator\ValidatorBuilder;

class CValidator {

    /** @var ValidatorInterface $validator */
    private $validator;

    public function __construct() {
        $this->validator = (new ValidatorBuilder())
            ->enableAnnotationMapping()
            ->getValidator();
    }

    public function validate ( $model ) : bool {
        /** @var ConstraintViolationList $errors */
        $errors = $this->validator->validate($model);
        foreach ( $errors as $error )
            echo "Error: {$error->getMessage()} \n";
        return count($errors) == 0;
    }

}