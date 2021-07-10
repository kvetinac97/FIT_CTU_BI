<?php declare(strict_types=1);


namespace App;

use App\Invoice\BusinessEntity;
use App\Invoice\Item;

class Invoice
{
    /** @var string */
    protected $number;

    /** @var BusinessEntity */
    protected $supplier;

    /** @var BusinessEntity */
    protected $customer;

    /** @var Item[]|array */
    protected $items;

    /**
     * @return string
     */
    public function getNumber(): string
    {
        return $this->number;
    }

    /**
     * @param string $number
     * @return Invoice
     */
    public function setNumber(string $number): Invoice
    {
        $this->number = $number;

        return $this;
    }

    /**
     * @return BusinessEntity
     */
    public function getSupplier(): BusinessEntity
    {
        return $this->supplier;
    }

    /**
     * @param BusinessEntity $supplier
     * @return Invoice
     */
    public function setSupplier(BusinessEntity $supplier): Invoice
    {
        $this->supplier = $supplier;

        return $this;
    }

    /**
     * @return BusinessEntity
     */
    public function getCustomer(): BusinessEntity
    {
        return $this->customer;
    }

    /**
     * @param BusinessEntity $customer
     * @return Invoice
     */
    public function setCustomer(BusinessEntity $customer): Invoice
    {
        $this->customer = $customer;

        return $this;
    }

    /**
     * @return array|Item[]
     */
    public function getItems()
    {
        return $this->items;
    }

    /**
     * @param Item $item
     * @return Invoice
     */
    public function addItem(Item $item): self
    {
        $this->items[] = $item;

        return $this;
    }

    public function getTotalPrice()
    {
        /** @var Item $previous */
        return array_reduce($this->items, function ($previous, $actual){
            return $previous + $actual->getTotalPrice();
        }, 0);
    }
}
