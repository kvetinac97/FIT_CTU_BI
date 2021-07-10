<?php

namespace HW\Lib;

class LinkedListItem
{
    /** @var string */
    protected $value;

    /** @var LinkedListItem|null */
    protected $next;

    /** @var LinkedListItem|null */
    protected $prev;

    /**
     * LinkedListItem constructor.
     *
     * @param string $value
     */
    public function __construct(string $value)
    {
        $this->value = $value;
    }

    /**
     * @return string
     */
    public function getValue(): string
    {
        return $this->value;
    }

    /**
     * @param string $value
     * @return LinkedListItem
     */
    public function setValue(string $value): LinkedListItem
    {
        $this->value = $value;

        return $this;
    }

    /**
     * @return LinkedListItem|null
     */
    public function getNext(): ?LinkedListItem
    {
        return $this->next;
    }

    /**
     * @param LinkedListItem|null $next
     * @return LinkedListItem
     */
    public function setNext(?LinkedListItem $next): LinkedListItem
    {
        $this->next = $next;

        return $this;
    }

    /**
     * @return LinkedListItem|null
     */
    public function getPrev(): ?LinkedListItem
    {
        return $this->prev;
    }

    /**
     * @param LinkedListItem|null $prev
     * @return LinkedListItem
     */
    public function setPrev(?LinkedListItem $prev): LinkedListItem
    {
        $this->prev = $prev;

        return $this;
    }

    public function equals ( ?LinkedListItem $other ) : bool {
        return $other !== null &&
            $this->value === $other->value &&
            $this->next === $other->next &&
            $this->prev === $other->prev;
    }

}
