<?php

namespace App\Entity;

use App\Repository\AccountRepository;
use DateTimeInterface;
use Doctrine\ORM\Mapping as ORM;
use JMS\Serializer\Annotation\Expose;
use JMS\Serializer\Annotation\ExclusionPolicy;
use JMS\Serializer\Annotation\MaxDepth;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity(repositoryClass=AccountRepository::class)
 * @ExclusionPolicy("all")
 */
class Account implements UserInterface, PasswordAuthenticatedUserInterface {

    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     * @Expose
     */
    private int $id;

    /**
     * @ORM\Column(type="string",length=64,unique=true)
     * @Assert\Length(min=6,minMessage="Name must have at least 6 characters",max=64,maxMessage="Name must have at most 64 characters")
     * @Expose
     */
    private string $name;

    /**
     * @ORM\Column(type="string")
     */
    private string $password;

    /**
     * @ORM\Column(type="json")
     */
    private array $roles = [];

    /**
     * @ORM\Column(type="datetime", nullable=true)
     * @Assert\Type("\DateTimeInterface")
     * @Assert\GreaterThan(value="today",message="Expiration must be in the future.")
     * @Expose
     */
    private ?DateTimeInterface $expiration;

    /**
     * @ORM\ManyToOne(targetEntity=Employee::class, inversedBy="accounts")
     * @ORM\JoinColumn(nullable=false)
     * @Expose
     * @MaxDepth(depth = 1)
     */
    private Employee $employee;

    /** @Expose */
    private bool $canEdit = false;

    public function getId() : int {
        return $this->id;
    }

    public function hasId () : bool {
        return isset($this->id);
    }

    public function getName() : string {
        return $this->name;
    }

    public function setName (string $name) : self {
        $this->name = $name;
        return $this;
    }

    public function getUserIdentifier() : string {
        return $this->name;
    }

    public function getUsername() : string {
        return $this->name;
    }

    public function getRoles() : array {
        $roles = $this->roles;
        $roles[] = 'ROLE_USER';
        return array_unique($roles);
    }

    public function setRoles (array $roles) : self {
        $this->roles = $roles;
        return $this;
    }

    public function getPassword() : string {
        return $this->password;
    }

    public function setPassword (string $password) : self {
        $this->password = $password;
        return $this;
    }

    public function getSalt() : ?string {
        return null;
    }

    public function eraseCredentials() {}

    public function getExpiration() : ?DateTimeInterface {
        return $this->expiration;
    }

    public function setExpiration (?DateTimeInterface $expiration) : self {
        $this->expiration = $expiration;
        return $this;
    }

    public function getEmployee() : Employee {
        return $this->employee;
    }

    public function setEmployee (Employee $employee) : self {
        $this->employee = $employee;
        return $this;
    }

    public function setCanEdit (bool $canEdit) : self {
        $this->canEdit = $canEdit;
        return $this;
    }

}
