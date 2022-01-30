<?php

namespace App\Form;

use App\Entity\User;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\TelType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class UserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('email', EmailType::class,[
                'label' => 'Email',
                'required' => true,
                'attr' => [
                    'placeholder' => 'example@email.com'
                ]
            ])
            ->add('name', TextType::class,[
                'label' => 'Name',
                'required' => true,
                'attr' => [
                    'placeholder' => 'Enter Name'
                ]
            ])
            ->add('tel', TelType::class,[
                'label' => 'Phone number',
                'required' => false,
                'empty_data' => null,
                'attr' => [
                    'placeholder' => 'e.g. 123 456 789'
                ]
            ])
            ->add('image', FileType::class, [
                'mapped' => false,
                'required' => false
            ]);
    }


    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => User::class,
        ]);
    }
}
