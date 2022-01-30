<?php

namespace App\FormType;

use Symfony\Component\Form\AbstractType as Type;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TelType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\UrlType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;

class EmployeeType extends Type {

    public function configureOptions (OptionsResolver $resolver) {
        $resolver->setDefault('create', true);
    }

    public function buildForm (FormBuilderInterface $builder, array $options) {
        $builder
            ->add('name', TextType::class, ['label' => 'Full name'])
            ->add('username', TextType::class, ['disabled' => !$options['create']])
            ->add('phone', TelType::class)
            ->add('email', EmailType::class)
            ->add('website', UrlType::class)
            ->add('description', TextareaType::class)
            ->add('photoFile', FileType::class, [
                'label' => 'Photo (image file)',
                'mapped' => false,
                'required' => false,
                'constraints' => [
                    new File([
                        'mimeTypes' => ['image/*'],
                        'mimeTypesMessage' => 'Please upload a valid image file',
                    ])
                ]
            ]);
    }

}