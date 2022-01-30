<?php

namespace App\Entity;

use App\Repository\RoomRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use JMS\Serializer\Annotation\SerializedName;
use JMS\Serializer\Annotation\VirtualProperty;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity(repositoryClass=RoomRepository::class)
 */
class Room
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     * @Groups({"room", "reservation", "building"})
     */
    private int $id;

    /**
     * @ORM\Column(type="string", length=128, unique=true)
     * @Assert\Length(min=3,minMessage="Name must have at least 3 characters",max=32,maxMessage="Name must have at most 32 characters")
     * @Groups({"room", "reservation", "building"})
     */
    private string $name;

    /**
     * @ORM\Column(type="boolean")
     * @Groups({"room", "reservation"})
     */
    private bool $public;

    /**
     * @ORM\OneToMany(targetEntity=Reservation::class, mappedBy="room")
     */
    private Collection $reservations;

    /**
     * @ORM\ManyToOne(targetEntity=Group::class, inversedBy="rooms")
     * @ORM\JoinColumn
     * @Groups("room")
     */
    private ?Group $group;

    /**
     * @ORM\ManyToOne(targetEntity=Building::class, inversedBy="rooms")
     * @ORM\JoinColumn(nullable=false)
     * @Assert\NotBlank
     * @Groups("room")
     */
    private Building $building;

    /**
     * @ORM\OneToMany(targetEntity=RoomUser::class, mappedBy="room", orphanRemoval=true)
     */
    private Collection $members;

    /**
     * @ORM\Column(type="boolean")
     */
    private bool $open;

    public function __construct()
    {
        $this->reservations = new ArrayCollection();
        $this->members = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(string $name): self
    {
        $this->name = $name;

        return $this;
    }

    public function getPublic(): ?bool
    {
        return $this->public;
    }

    public function setPublic(bool $public): self
    {
        $this->public = $public;

        return $this;
    }

    /**
     * @return Collection|Reservation[]
     */
    public function getReservations(): Collection
    {
        return $this->reservations;
    }

    public function addReservation(Reservation $reservation): self
    {
        if (!$this->reservations->contains($reservation)) {
            $this->reservations[] = $reservation;
            $reservation->setRoom($this);
        }

        return $this;
    }

    public function removeReservation(Reservation $reservation): self
    {
        $this->reservations->removeElement($reservation);
        return $this;
    }

    public function getGroup(): ?Group
    {
        return $this->group;
    }

    public function setGroup(?Group $group): self
    {
        $this->group = $group;
        return $this;
    }

    public function getBuilding(): Building
    {
        return $this->building;
    }

    public function setBuilding(Building $building): self
    {
        $this->building = $building;
        return $this;
    }

    /**
     * @return Collection|RoomUser[]
     */
    public function getMembers(): Collection
    {
        return $this->members;
    }

//    /**
//     * @VirtualProperty
//     * @SerializedName("managers")
//     * @Groups({"room"})
//     * @return Collection|User[]
//     */
//    public function getManagers(): Collection
//    {
//        return $this->members
//            ->filter(fn(RoomUser $roomUser) => $roomUser->getManager())
//            ->map(fn(RoomUser $roomUser) => $roomUser->getUser());
//    }

    public function addMember(RoomUser $member): self
    {
        if (!$this->members->contains($member)) {
            $this->members[] = $member;
            $member->setRoom($this);
        }

        return $this;
    }

    public function removeMember(RoomUser $member): self
    {
        if ($this->members->removeElement($member)) {
            // set the owning side to null (unless already changed)
            if ($member->getRoom() === $this) {
                $member->setRoom(null);
            }
        }

        return $this;
    }

    public function getOpen(): bool
    {
        return $this->open;
    }

    public function setOpen(bool $open): self
    {
        $this->open = $open;

        return $this;
    }
}
