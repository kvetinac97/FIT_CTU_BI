<?php

namespace App\Validator;

use Symfony\Component\Validator\Constraint;

/**
 * @Annotation
 */
class ReservationLogic extends Constraint
{
    public $message = 'Reservation cannot be approved and cancelled at the same time!';

    public function getTargets(): string
    {
        return self::CLASS_CONSTRAINT;
    }
}
