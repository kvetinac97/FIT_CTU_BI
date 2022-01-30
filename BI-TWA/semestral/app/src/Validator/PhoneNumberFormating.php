<?php

namespace App\Validator;

use Symfony\Component\Validator\Constraint;

/**
 * @Annotation
 */
class PhoneNumberFormating extends Constraint
{
    public $message = 'Phone number {{ tel }} should be of format xxx xxx xxx.';
}
