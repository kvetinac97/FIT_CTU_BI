<?php

namespace App\Entity;

use App\Repository\RoomUserRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

/**
 * @ORM\Entity(repositoryClass=RoomUserRepository::class)
 */
class RoomUser
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     * @Groups({"roomMembers"})
     */
    private int $id;

    /**
     * @ORM\Column(type="boolean")
     * @Groups({"roomMembers"})
     */
    private bool $manager;

    /**
     * @ORM\ManyToOne(targetEntity=Room::class, inversedBy="members")
     * @ORM\JoinColumn(nullable=false)
     */
    private Room $room;

    /**
     * @ORM\ManyToOne(targetEntity=User::class, inversedBy="roomMemberships")
     * @ORM\JoinColumn(nullable=false)
     * @Groups({"roomMembers", "roomManager"})
     */
    private User $user;


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getManager(): ?bool
    {
        return $this->manager;
    }

    public function setManager(bool $manager): self
    {
        $this->manager = $manager;
        return $this;
    }

    public function getRoom(): ?Room
    {
        return $this->room;
    }

    public function setRoom(?Room $room): self
    {
        $this->room = $room;
        return $this;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(?User $user): self
    {
        $this->user = $user;
        return $this;
    }
}
