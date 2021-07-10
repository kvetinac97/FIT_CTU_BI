<?php declare(strict_types=1);


namespace App;

use App\Invoice\Address;
use App\Invoice\BusinessEntity;
use App\Invoice\Item;

class Builder
{
    /** @var Invoice */
    protected $invoice;

    /**
     * @return Invoice
     */
    public function build(): Invoice
    {
        return $this->invoice;
    }

    public function __construct() {
        $this->invoice = new Invoice();
    }

    /**
     * @param string $number
     * @return $this
     */
    public function setNumber(string $number): self
    {
        $this->invoice->setNumber($number);
        return $this;
    }


    /**
     * @param string      $name
     * @param string      $vatNumber
     * @param string      $street
     * @param string      $number
     * @param string      $city
     * @param string      $zip
     * @param string|null $phone
     * @param string|null $email
     * @return $this
     */
    public function setSupplier(
        string $name,
        string $vatNumber,
        string $street,
        string $number,
        string $city,
        string $zip,
        ?string $phone = null,
        ?string $email = null
    ): self {
        $this->invoice->setSupplier((new BusinessEntity())
            ->setName($name)
            ->setVatNumber($vatNumber)
            ->setAddress(
                (new Address())
                    ->setStreet($street)
                    ->setNumber($number)
                    ->setCity($city)
                    ->setZipCode($zip)
                    ->setPhone($phone)
                    ->setEmail($email)
            )
        );
        return $this;
    }

    /**
     * @param string      $name
     * @param string      $vatNumber
     * @param string      $street
     * @param string      $number
     * @param string      $city
     * @param string      $zip
     * @param string|null $phone
     * @param string|null $email
     * @return $this
     */
    public function setCustomer(
        string $name,
        string $vatNumber,
        string $street,
        string $number,
        string $city,
        string $zip,
        ?string $phone = null,
        ?string $email = null
    ): self {
        $this->invoice->setCustomer((new BusinessEntity())
            ->setName($name)
            ->setVatNumber($vatNumber)
            ->setAddress(
                (new Address())
                    ->setStreet($street)
                    ->setNumber($number)
                    ->setCity($city)
                    ->setZipCode($zip)
                    ->setPhone($phone)
                    ->setEmail($email)
            )
        );
        return $this;
    }

    /**
     * @param string     $description
     * @param float|null $quantity
     * @param float|null $price
     * @return $this
     */
    public function addItem(string $description, ?float $quantity, ?float $price): self
    {
        $this->invoice->addItem(
            (new Item())
            ->setDescription($description)
            ->setQuantity($quantity ?? 1.0)
            ->setUnitPrice($price ?? 0)
        );
        return $this;
    }
}
