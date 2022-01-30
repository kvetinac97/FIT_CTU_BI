<?php

namespace App\Entity;

use App\Repository\EmployeeRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use JetBrains\PhpStorm\Pure;
use JMS\Serializer\Annotation\Expose;
use JMS\Serializer\Annotation\ExclusionPolicy;
use JMS\Serializer\Annotation\MaxDepth;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity(repositoryClass=EmployeeRepository::class)
 * @ExclusionPolicy("all")
 */
class Employee {

    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     * @Expose
     */
    private int $id;

    /**
     * @ORM\Column(type="string", length=64)
     * @Assert\Length(min=6,minMessage="Name must have at least 6 characters",max=64,maxMessage="Name must have at most 64 characters")
     * @Expose
     */
    private string $name;

    /**
     * @ORM\Column(type="string", length=8, unique=true)
     * @Assert\Length(min=4,max=8,minMessage="Username must have at least 4 characters",maxMessage="Username must have at most 8 characters")
     * @Expose
     */
    private string $username;

    /**
     * @ORM\Column(type="string", length=16)
     * @Assert\Length(min=9,minMessage="Number must have at least 9 characters",max=16,maxMessage="Phone must have at most 16 characters")
     */
    private string $phone;

    /**
     * @ORM\Column(type="string", length=255)
     * @Assert\NotBlank
     * @Assert\Email
     * @Expose
     */
    private string $email;

    /**
     * @ORM\Column(type="string", length=255)
     * @Assert\NotBlank
     * @Assert\Url
     */
    private string $website;

    /**
     * @ORM\Column(type="text")
     * @Assert\NotBlank
     * @Assert\Length(min=30,max=600,minMessage="Description must be at least 30 characters",maxMessage="Description must be at most 600 characters")
     */
    private string $description;

    /**
     * @ORM\OneToMany(targetEntity=Account::class, mappedBy="employee", orphanRemoval=true, cascade={"remove"})
     * @Expose
     * @MaxDepth(depth = 1)
     */
    private Collection $accounts;

    /**
     * @ORM\ManyToMany(targetEntity=Role::class)
     * @Expose
     */
    private Collection $roles;

    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     * @Expose
     */
    private ?string $photo;

    /** @Expose */
    private bool $canEdit = false;

    /** @Expose */
    private bool $canView = false;

    #[Pure] public function __construct() {
        $this->accounts = new ArrayCollection();
        $this->roles = new ArrayCollection();
        $this->photo = null;
    }

    public function hasId() : bool {
        return isset($this->id);
    }

    public function getId() : int {
        return $this->id;
    }

    public function getName() : string {
        return $this->name;
    }

    public function setName (string $name) : self {
        $this->name = $name;
        return $this;
    }

    public function getUsername() : string {
        return $this->username;
    }

    public function setUsername (string $username) : self {
        $this->username = $username;
        return $this;
    }

    public function getPhone() : ?string {
        return $this->phone;
    }

    public function setPhone (string $phone) : self {
        $this->phone = $phone;
        return $this;
    }

    public function getEmail() : string {
        return $this->email;
    }

    public function setEmail(string $email) : self {
        $this->email = $email;
        return $this;
    }

    public function getWebsite() : string {
        return $this->website;
    }

    public function setWebsite (string $website) : self {
        $this->website = $website;
        return $this;
    }

    public function getDescription() : ?string {
        return $this->description;
    }

    public function setDescription (string $description) : self {
        $this->description = $description;
        return $this;
    }

    public function getAccounts() : Collection {
        return $this->accounts;
    }

    public function addAccount (Account $account) : self {
        if (!$this->accounts->contains($account)) {
            $this->accounts[] = $account;
            $account->setEmployee($this);
        }

        return $this;
    }

    public function removeAccount (Account $account) : self  {
        $this->accounts->removeElement($account);
        return $this;
    }

    public function getRoles() : Collection {
        return $this->roles;
    }

    public function addRole (Role $role) : self {
        if (!$this->roles->contains($role))
            $this->roles[] = $role;
        return $this;
    }

    public function removeRole (Role $role) : self {
        $this->roles->removeElement($role);
        return $this;
    }

    public function getPhoto() : ?string {
        return $this->photo;
    }

    public function setPhoto (?string $photo) : self {
        $this->photo = $photo;
        return $this;
    }

    public function setCanEdit (bool $canEdit) : self {
        $this->canEdit = $canEdit;
        return $this;
    }

    public function setCanView (bool $canView) : self {
        $this->canView = $canView;
        return $this;
    }

}
