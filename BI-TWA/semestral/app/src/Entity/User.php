<?php

namespace App\Entity;

use App\Repository\UserRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints as Assert;
use App\Validator\PhoneNumberFormating;

/**
 * @ORM\Entity(repositoryClass=UserRepository::class)
 * @ORM\Table(name="`user`")
 */
class User
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     * @Groups({"user", "reservation", "account", "room", "roomMembers", "groupMembers",  "groupManager"})
     */
    private ?int $id = null;

    /**
     * @ORM\Column(type="string", length=255, unique=true)
     * @Assert\NotBlank
     * @Assert\Email
     * @Groups({"user", "reservation", "account", "room", "group"})
     */
    private string $email;

    /**
     * @ORM\Column(type="string", length=128)
     * @Assert\NotBlank
     * @Groups({"user", "reservation", "account", "room", "group", "roomMembers", "groupMembers", "groupManager"})
     */
    private string $name;

    /**
     * @ORM\Column(type="string", length=16, nullable=false)
     * @Assert\NotBlank
     * @PhoneNumberFormating
     * @Groups({"user", "reservation", "account", "room", "group"})
     */
    private string $tel;

    /**
     * @ORM\OneToMany(targetEntity=Reservation::class, mappedBy="author")
     */
    private Collection $authorReservations;

    /**
     * @ORM\ManyToMany(targetEntity=Reservation::class, mappedBy="users")
     */
    private Collection $reservations;

    /**
     * @Groups({"user"})
     */
    private ?array $roles = [];

    /**
     * @ORM\OneToMany(targetEntity=Member::class, mappedBy="user", orphanRemoval=true)
     */
    private Collection $groupMemberships;

    /**
     * @ORM\OneToMany(targetEntity=RoomUser::class, mappedBy="user", orphanRemoval=true)
     */
    private Collection $roomMemberships;

    /**
     * @ORM\OneToMany(targetEntity=Account::class, mappedBy="user", orphanRemoval=true)
     */
    private Collection $accounts;

    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     * @Groups({"user", "reservation", "account"})
     */
    private ?string $image;

    public function __construct()
    {
        $this->authorReservations = new ArrayCollection();
        $this->reservations = new ArrayCollection();
        $this->groupMemberships = new ArrayCollection();
        $this->roomMemberships = new ArrayCollection();
        $this->accounts = new ArrayCollection();
    }

    public function hasId(): bool {
        return isset($this->id);
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): self
    {
        $this->email = $email;

        return $this;
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

    public function getTel(): ?string
    {
        return $this->tel;
    }

    public function setTel(?string $tel): self
    {
        $this->tel = $tel;

        return $this;
    }

    /**
     * @return Collection|Reservation[]
     */
    public function getAuthorReservations(): Collection
    {
        return $this->authorReservations;
    }

    public function addAuthorReservation(Reservation $reservation): self
    {
        if (!$this->authorReservations->contains($reservation)) {
            $this->authorReservations[] = $reservation;
            $reservation->setAuthor($this);
        }

        return $this;
    }

    public function removeAuthorReservation(Reservation $reservation): self
    {
        $this->authorReservations->removeElement($reservation);
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
            $reservation->addUser($this);
        }

        return $this;
    }

    public function removeReservation(Reservation $reservation): self
    {
        $this->reservations->removeElement($reservation);
        $reservation->removeUser($this);
        return $this;
    }

    public function getRoles(): array
    {
        $roles = $this->roles;
        // guarantee every user at least has ROLE_USER
        $roles[] = 'ROLE_USER';

        foreach ($this->getAccounts() as $account)
            $roles = array_merge($roles, $account->getRoles());

        return array_unique($roles);
    }

    /**
     * @return Collection|Member[]
     */
    public function getGroupMemberships(): Collection
    {
        return $this->groupMemberships;
    }

    public function addGroupMembership(Member $groupMembership): self
    {
        if (!$this->groupMemberships->contains($groupMembership)) {
            $this->groupMemberships[] = $groupMembership;
            $groupMembership->setUser($this);
        }

        return $this;
    }

    public function removeGroupMembership(Member $groupMembership): self
    {
        if ($this->groupMemberships->removeElement($groupMembership)) {
            // set the owning side to null (unless already changed)
            if ($groupMembership->getUser() === $this) {
                $groupMembership->setUser(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection|RoomUser[]
     */
    public function getRoomMemberships(): Collection
    {
        return $this->roomMemberships;
    }

    public function addRoomMembership(RoomUser $roomMembership): self
    {
        if (!$this->roomMemberships->contains($roomMembership)) {
            $this->roomMemberships[] = $roomMembership;
            $roomMembership->setUser($this);
        }

        return $this;
    }

    public function removeRoomMembership(RoomUser $roomMembership): self
    {
        if ($this->roomMemberships->removeElement($roomMembership)) {
            // set the owning side to null (unless already changed)
            if ($roomMembership->getUser() === $this) {
                $roomMembership->setUser(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection|Account[]
     */
    public function getAccounts(): Collection
    {
        return $this->accounts;
    }

    public function addAccount(Account $account): self
    {
        if (!$this->accounts->contains($account)) {
            $this->accounts[] = $account;
            $account->setUser($this);
        }

        return $this;
    }

    public function removeAccount(Account $account): self
    {
        $this->accounts->removeElement($account);
        return $this;
    }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(?string $image): self
    {
        $this->image = $image;

        return $this;
    }

}
