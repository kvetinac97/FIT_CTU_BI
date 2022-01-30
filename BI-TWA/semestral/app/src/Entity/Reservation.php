<?php

namespace App\Entity;

use App\Repository\ReservationRepository;
use App\Validator\ReservationLogic;
use DateTimeInterface;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use JMS\Serializer\Annotation\SerializedName;
use JMS\Serializer\Annotation\VirtualProperty;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity(repositoryClass=ReservationRepository::class)
 * @ReservationLogic
 */
class Reservation
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     * @Groups("reservation")
     */
    private int $id;

    /**
     * @ORM\Column(type="boolean")
     * @Groups("reservation")
     */
    private bool $approved;

    /**
     * @ORM\Column(type="boolean")
     * @Groups("reservation")
     */
    private bool $cancelled;

    /**
     * @ORM\Column(type="datetime")
     * @Assert\Type("\DateTimeInterface")
     * @Assert\GreaterThan(value="today",message="Start must be in the future.")
     * @Assert\NotBlank
     * @Groups("reservation")
     */
    private DateTimeInterface $start;

    /**
     * @ORM\Column(type="datetime")
     * @Assert\Type("\DateTimeInterface")
     * @Assert\GreaterThan(value="today",message="End must be in the future.")
     * @Assert\GreaterThan(propertyPath="start",message="End must be after start.")
     * @Assert\NotBlank
     * @Groups("reservation")
     */
    private DateTimeInterface $until;

    /**
     * @ORM\ManyToOne(targetEntity=User::class, inversedBy="authorReservations")
     * @ORM\JoinColumn(nullable=false)
     * @Assert\NotBlank
     * @Groups("reservation")
     */
    private User $author;

    /**
     * @ORM\ManyToOne(targetEntity=Room::class, inversedBy="reservations")
     * @ORM\JoinColumn(nullable=false)
     * @Assert\NotBlank
     * @Groups("reservation")
     */
    private Room $room;


    /**
     * @ORM\ManyToMany(targetEntity=User::class, inversedBy="reservations")
     */
    private Collection $users;

    public function __construct()
    {
        $this->users = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getApproved(): ?bool
    {
        return $this->approved;
    }

    public function setApproved(bool $approved): self
    {
        $this->approved = $approved;

        return $this;
    }

    public function getCancelled(): ?bool
    {
        return $this->cancelled;
    }

    public function setCancelled(bool $cancelled): self
    {
        $this->cancelled = $cancelled;

        return $this;
    }

    /**
     * @VirtualProperty
     * @SerializedName("status")
     * @Groups({"reservation"})
     */
    public function getStatus(): string
    {
        if ($this->cancelled) return "Cancelled";
        if ($this->approved) return "Approved";
        return "Waiting";
    }

    public function getStart(): ?DateTimeInterface
    {
        return $this->start;
    }

    public function setStart(DateTimeInterface $start): self
    {
        $this->start = $start;

        return $this;
    }

    public function getUntil(): ?DateTimeInterface
    {
        return $this->until;
    }

    public function setUntil(DateTimeInterface $until): self
    {
        $this->until = $until;

        return $this;
    }

    public function getAuthor(): User
    {
        return $this->author;
    }

    public function setAuthor(User $author): self
    {
        $this->author = $author;
        return $this;
    }

    public function getRoom(): Room
    {
        return $this->room;
    }

    public function setRoom(Room $room): self
    {
        $this->room = $room;
        return $this;
    }


    public function getUsers(): Collection
    {
        return $this->users;
    }

    public function addUser(User $user): self
    {
        if (!$this->users->contains($user)) {
            $this->users[] = $user;
        }

        return $this;
    }

    public function removeUser(User $user): self
    {
        $this->users->removeElement($user);

        return $this;
    }
}
