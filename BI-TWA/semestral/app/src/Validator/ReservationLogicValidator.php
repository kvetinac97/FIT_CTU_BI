<?php

namespace App\Validator;

use App\Entity\Group;
use App\Entity\Reservation;
use Symfony\Component\Validator\Constraint;
use Symfony\Component\Validator\ConstraintValidator;
use Symfony\Component\Validator\Exception\UnexpectedTypeException;
use Symfony\Component\Validator\Exception\UnexpectedValueException;

class ReservationLogicValidator extends ConstraintValidator
{
    public function validate($value, Constraint $constraint)
    {
        if (!$constraint instanceof ReservationLogic)
            throw new UnexpectedTypeException($constraint, ReservationLogic::class);

        if (null === $value || '' === $value)
            return;

        if (!$value instanceof Reservation)
            throw new UnexpectedValueException($value, 'App\\Entity\\Reservation');

        if ($value->getCancelled() && $value->getApproved())
            $this->context->buildViolation($constraint->message)
                ->addViolation();
    }
}