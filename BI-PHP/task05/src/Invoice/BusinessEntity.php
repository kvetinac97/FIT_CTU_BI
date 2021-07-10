<?php declare(strict_types=1);


namespace App\Invoice;


class BusinessEntity
{
    /** @var string */
    protected $name;

    /** @var string */
    protected $vatNumber;

    /** @var Address */
    protected $address;

    /**
     * @return string
     */
    public function getName(): string
    {
        return $this->name;
    }

    /**
     * @param string $name
     * @return BusinessEntity
     */
    public function setName(string $name): BusinessEntity
    {
        $this->name = $name;

        return $this;
    }

    /**
     * @return string
     */
    public function getVatNumber(): string
    {
        return $this->vatNumber;
    }

    /**
     * @param string $vatNumber
     * @return BusinessEntity
     */
    public function setVatNumber(string $vatNumber): BusinessEntity
    {
        $this->vatNumber = $vatNumber;

        return $this;
    }

    /**
     * @return Address
     */
    public function getAddress(): Address
    {
        return $this->address;
    }

    /**
     * @param Address $address
     * @return BusinessEntity
     */
    public function setAddress(Address $address): BusinessEntity
    {
        $this->address = $address;

        return $this;
    }
}
