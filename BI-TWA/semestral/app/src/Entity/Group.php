<?php

namespace App\Entity;

use App\Validator\GroupAcyclic;
use App\Repository\GroupRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use JMS\Serializer\Annotation\SerializedName;
use JMS\Serializer\Annotation\VirtualProperty;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity(repositoryClass=GroupRepository::class)
 * @ORM\Table(name="`group`")
 * @UniqueEntity("name")
 * @GroupAcyclic
 */
class Group
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     * @Groups({"group", "room"})
     */
    private int $id;

    /**
     * @ORM\Column(type="string", length=128, unique=true)
     * @Assert\Length(min=3,minMessage="Name must have at least 3 characters",max=32,maxMessage="Name must have at most 32 characters")
     * @Groups({"group", "room"})
     */
    private string $name;

    /**
     * @ORM\ManyToOne(targetEntity=Group::class, inversedBy="child_groups")
     */
    private ?Group $parent_group;

    /**
     * @ORM\OneToMany(targetEntity=Group::class, mappedBy="parent_group")
     */
    private Collection $child_groups;

    /**
     * @ORM\OneToMany(targetEntity=Room::class, mappedBy="group")
     */
    private Collection $rooms;

    /**
     * @ORM\OneToMany(targetEntity=Member::class, mappedBy="group")
     */
    private Collection $members;

    public function __construct()
    {
        $this->rooms = new ArrayCollection();
        $this->members = new ArrayCollection();
        $this->child_groups = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function hasId() : bool
    {
        return isset($this->id);
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

    public function getParentGroup(): ?self
    {
        return $this->parent_group;
    }

    public function setParentGroup(?self $parent_group): self
    {
        $this->parent_group = $parent_group;
        return $this;
    }

    /**
     * @return Collection|Room[]
     */
    public function getRooms(): Collection
    {
        return $this->rooms;
    }

    public function addRoom(Room $room): self
    {
        if (!$this->rooms->contains($room)) {
            $this->rooms[] = $room;
            $room->setGroup($this);
        }

        return $this;
    }

    public function removeRoom(Room $room): self
    {
        $this->rooms->removeElement($room);
        return $this;
    }

    /**
     * @return Collection|Member[]
     */
    public function getMembers(): Collection
    {
        return $this->members;
    }

//    /**
//     * @VirtualProperty
//     * @SerializedName("managers")
//     * @Groups({"group"})
//     * @return Collection|User[]
//     */
//    public function getManagers(): Collection
//    {
//        return $this->members
//            ->filter(fn(Member $member) => $member->getManager())
//            ->map(fn(Member $member) => $member->getUser());
//    }

    /**
     * @return Collection|Group[]
     */
    public function getChildren(): Collection
    {
        return $this->child_groups;
    }
}
