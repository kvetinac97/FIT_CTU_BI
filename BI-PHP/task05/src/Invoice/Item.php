<?php declare(strict_types=1);


namespace App\Invoice;


class Item
{
    /** @var string */
    protected $description;

    /** @var float */
    protected $quantity = 1.0;

    /** @var float */
    protected $unitPrice;

    /**
     * @return string
     */
    public function getDescription(): string
    {
        return $this->description;
    }

    /**
     * @param string $description
     * @return Item
     */
    public function setDescription(string $description): Item
    {
        $this->description = $description;

        return $this;
    }

    /**
     * @return float
     */
    public function getQuantity(): float
    {
        return $this->quantity;
    }

    /**
     * @param float $quantity
     * @return Item
     */
    public function setQuantity(float $quantity): Item
    {
        $this->quantity = $quantity;

        return $this;
    }

    /**
     * @return float
     */
    public function getUnitPrice(): float
    {
        return $this->unitPrice;
    }

    /**
     * @param float $unitPrice
     * @return Item
     */
    public function setUnitPrice(float $unitPrice): Item
    {
        $this->unitPrice = $unitPrice;

        return $this;
    }

    /**
     * @return float|int
     */
    public function getTotalPrice()
    {
        return $this->unitPrice * $this->quantity;
    }
}
