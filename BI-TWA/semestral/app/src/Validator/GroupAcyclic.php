<?php

namespace App\Validator;

use Symfony\Component\Validator\Constraint;

/**
 * @Annotation
 */
class GroupAcyclic extends Constraint
{
    public $message = '{{ group }} is descendant of the current group and assigning would create a cycle.';

    public function getTargets(): string
    {
        return self::CLASS_CONSTRAINT;
    }
}
