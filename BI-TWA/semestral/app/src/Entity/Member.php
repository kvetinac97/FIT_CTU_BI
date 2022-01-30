<?php

namespace App\Entity;

use App\Repository\MemberRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

/**
 * @ORM\Entity(repositoryClass=MemberRepository::class)
 * @ORM\Table(uniqueConstraints={
 *     @ORM\UniqueConstraint(name="uix_member_user_id_group_id", columns={"user_id", "group_id"})
 * })
 */
class Member
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     * @Groups({"groupMembers"})
     */
    private int $id;

    /**
     * @ORM\Column(type="boolean")
     * @Groups({"groupMembers"})
     */
    private bool $manager;

    /**
     * @ORM\ManyToOne(targetEntity=User::class, inversedBy="groupMemberships")
     * @ORM\JoinColumn(nullable=false)
     * @Groups({"groupMembers", "groupManager"})
     */
    private User $user;

    /**
     * @ORM\ManyToOne(targetEntity=Group::class, inversedBy="members")
     * @ORM\JoinColumn(nullable=false)
     */
    private Group $group;


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

    public function getUser(): User
    {
        return $this->user;
    }

    public function setUser(User $user): self
    {
        $this->user = $user;
        return $this;
    }

    public function getGroup(): Group
    {
        return $this->group;
    }

    public function setGroup(Group $group): self
    {
        $this->group = $group;
        return $this;
    }
}
