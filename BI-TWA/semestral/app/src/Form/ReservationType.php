<?php

namespace App\Form;

use App\Entity\Reservation;
use App\Entity\Room;
use App\Entity\User;
use App\Repository\RoomRepository;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class ReservationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('room', EntityType::class, [
				'label' => 'Room',
				'class' => Room::class,
				'choice_label' => 'name',
                'query_builder' => fn (RoomRepository $r) =>
                    $r->createQueryBuilder('room')
                        ->where('room.id IN (:ids)')
                        ->setParameter('ids', $options['room_ids']),
                'required' => true,
                'disabled' => !$options['room_selection']
			]);

        if ($options['rest'] || !$options['room_selection'])
            $builder
                ->add('start', DateTimeType::class,[
                    'label' => 'Start',
                    'required' => true,
//                    'disabled' => $options['edit']
                ])
                ->add('until', DateTimeType::class,[
                    'label' => 'Until',
                    'required' => true,
//                    'disabled' => $options['edit']
                ]);

        if ($options['rest'] || $options['can_approve'])
            $builder
                ->add('author', EntityType::class, [
                    'label' => 'Author',
                    'class' => User::class,
                    'choice_label' => 'name',
                    'required' => true,
//                    'disabled' => $options['edit']
                ])
                ->add('approved', CheckboxType::class,[
                    'label' => 'Approved',
                    'required' => false,
                    'value' => false
                ])
                ->add('cancelled', CheckboxType::class,[
                    'label' => 'Cancelled',
                    'required' => false,
                    'value' => false
                ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Reservation::class,
            'room_ids' => [],
            'can_approve' => false,
            'room_selection' => false,
            'edit' => false,
            'rest' => false,
        ]);
    }
}
