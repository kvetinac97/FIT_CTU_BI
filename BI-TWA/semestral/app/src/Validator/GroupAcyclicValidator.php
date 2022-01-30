<?php

namespace App\Validator;

use App\Entity\Group;
use Symfony\Component\Validator\Constraint;
use Symfony\Component\Validator\ConstraintValidator;
use Symfony\Component\Validator\Exception\UnexpectedTypeException;
use Symfony\Component\Validator\Exception\UnexpectedValueException;

class GroupAcyclicValidator extends ConstraintValidator
{
    public function validate($value, Constraint $constraint)
    {
        if (!$constraint instanceof GroupAcyclic)
            throw new UnexpectedTypeException($constraint, GroupAcyclic::class);

        if (null === $value || '' === $value)
            return;

        if (!$value instanceof Group)
            throw new UnexpectedValueException($value, 'App\\Entity\\Group');

        if (!$value->hasId()) return; // there can be no cycle in new groups

        $group = $value;

        while (($parent = $group->getParentGroup()) !== null) {
            // Cycle detected
            if ($parent->getId() === $value->getId()) {
                $this->context->buildViolation($constraint->message)
                    ->setParameter('{{ group }}', $value->getParentGroup()->getName())
                    ->addViolation();
                return;
            }
            $group = $parent;
        }
    }
}