<?php

namespace App\Validator;

use App\Entity\User;
use Symfony\Component\Validator\Constraint;
use Symfony\Component\Validator\ConstraintValidator;
use Symfony\Component\Validator\Exception\UnexpectedTypeException;
use Symfony\Component\Validator\Exception\UnexpectedValueException;

class PhoneNumberFormatingValidator extends ConstraintValidator
{
    public function validate($value, Constraint $constraint)
    {
        if (!$constraint instanceof PhoneNumberFormating)
            throw new UnexpectedTypeException($constraint, PhoneNumberFormating::class);

        if (null === $value || '' === $value)
            return;

        if (!preg_match('/^([0-9]){3} ([0-9]){3} ([0-9]){3}$/', $value, $matches)) {
            $this->context->buildViolation($constraint->message)
                ->setParameter('{{ tel }}', $value)
                ->addViolation();
        }
    }
}