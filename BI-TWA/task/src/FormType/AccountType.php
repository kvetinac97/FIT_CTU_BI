<?php

namespace App\FormType;

use App\Entity\Employee;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType as Type;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\Length;

class AccountType extends Type {

    public function configureOptions (OptionsResolver $resolver) {
        $resolver->setDefault('restEdit', false);
        $resolver->setDefault('create', false);
    }

    public function buildForm (FormBuilderInterface $builder, array $options) {
        $builder
            ->add('name', TextType::class, ['label' => 'Account name', 'disabled' => $options['restEdit']])
            ->add('plain_password', PasswordType::class, [
                'label' => 'Password',
                'mapped' => false,
                'required' => $options['create'],
                'constraints' => [
                    new Length(null, 6, null, null, null,
                        null, "Password must have at least 6 characters")
                ]
            ])
            ->add('employee', EntityType::class, [
                'label' => 'Employee',
                'class' => Employee::class,
                'choice_label' => fn (Employee $employee) => "{$employee->getName()} (#{$employee->getId()})",
                'disabled' => true,
            ])
            ->add('expiration', DateTimeType::class, ['required' => false]);
    }

}