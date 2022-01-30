<?php

namespace App\Form;

use App\Entity\Account;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class AccountType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name', TextType::class,[
                'label' => 'UserName',
                'required' => true,
                'attr' => [
                    'placeholder' => 'Enter Username'
                ]
            ])
            ->add('password', PasswordType::class,[
                'label' => 'Password',
                'required' => true,
                'attr' => [
                    'placeholder' => 'At least 6 characters'
                ]
            ])
            ->add('expiration', DateTimeType::class,[
                'label' => 'Expiration',
                'required' => $options['only_temporary'],
            ]);
    }


    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Account::class,
            'only_temporary' => false,
        ]);
    }
}
