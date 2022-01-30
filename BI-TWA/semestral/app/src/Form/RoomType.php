<?php

namespace App\Form;

use App\Entity\Room;
use App\Entity\Building;
use App\Entity\Group;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class RoomType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name', TextType::class,[
                'label' => 'Name',
                'required' => true,
                'attr' => [
                    'placeholder' => 'Enter Room Name'
                ]
            ])
            ->add('public', CheckboxType::class,[
                'label' => 'Public',
                'value' => false,
                'required' => false
            ])
            ->add('building', EntityType::class, [
				'label' => 'Building',
				'class' => Building::class,
				'choice_label' => 'name',
                'required' => true
			])
            ->add('group', EntityType::class, [
				'label' => 'Group',
				'class' => Group::class,
				'choice_label' => 'name',
                'required' => false
			]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Room::class,
        ]);
    }
}
