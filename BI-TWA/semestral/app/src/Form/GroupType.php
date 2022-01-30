<?php

namespace App\Form;

use App\Entity\Group;
use App\Repository\GroupRepository;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class GroupType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name', TextType::class,[
                'label' => 'Name',
                'required' => true,
                'attr' => [
                    'placeholder' => 'Enter Group Name'
                ]
            ])
            ->add('parent_group', EntityType::class,[
                'label' => 'Parent group',
                'class' => Group::class,
                'query_builder' => fn (GroupRepository $gr) => $gr->createQueryBuilder('g')
                        ->where('g.id != :id')
                        ->setParameter('id', $options['group_id']),
                'choice_label' => fn (Group $group) => $group->getName(),
                'placeholder' => '(No parent group)',
                'required' => false,
                'empty_data' => null
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Group::class,
            'group_id' => -1
        ]);
    }
}
