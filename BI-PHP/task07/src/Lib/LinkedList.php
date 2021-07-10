<?php

namespace HW\Lib;

class LinkedList
{
    /** @var LinkedListItem|null */
    protected $first = null;

    /** @var LinkedListItem|null */
    protected $last = null;

    /**
     * @return LinkedListItem|null
     */
    public function getFirst(): ?LinkedListItem
    {
        return $this->first;
    }

    /**
     * @param LinkedListItem|null $first
     * @return LinkedList
     */
    public function setFirst(?LinkedListItem $first): LinkedList
    {
        if ($first === null) {
            $this->first = $this->last = null;
            return $this;
        }

        $original = $this->first;
        $this->first = $first;

        // Paranoid
        if ($first->getPrev() !== null) {
            $first->getPrev()->setNext($first->getNext());
            if ( $first->getNext() !== null )
                $first->getNext()->setPrev($first->getPrev());
            $first->setPrev(null);
        }

        if ($original === null)
            $this->last = $first;
        else {
            $this->first->setNext($original);
            $original->setPrev($this->first);
        }

        return $this;
    }

    /**
     * @return LinkedListItem|null
     */
    public function getLast(): ?LinkedListItem
    {
        return $this->last;
    }

    /**
     * @param LinkedListItem|null $last
     * @return LinkedList
     */
    public function setLast(?LinkedListItem $last): LinkedList
    {
        $original = $this->last;
        if ($last === null) {
            if ($original !== null && $original->getPrev() !== null) {
                $original->getPrev()->setNext(null);
                $this->last = $original->getPrev();
            }
            else {
                if ($this->last === $this->first)
                    $this->first = null;
                $this->last = null;
            }
            return $this;
        }

        $this->last = $last;

        // Paranoid
        if ($last->getNext() !== null) {
            $last->getNext()->setPrev($last->getPrev());
            if ( $last->getPrev() !== null )
                $last->getPrev()->setNext($last->getNext());
            $last->setNext(null);
        }

        if ($original === null)
            $this->first = $last;
        else {
            $this->last->setPrev($original);
            $original->setNext($this->last);
        }

        return $this;
    }

    /**
     * Place new item at the begining of the list
     *
     * @param string $value
     * @return LinkedListItem
     */
    public function prependList(string $value)
    {
        $item = new LinkedListItem($value);
        if ($this->first === null) {
            $this->setFirst($item);
            return $item;
        }

        return $this->prependItem($this->first, $value);
    }

    /**
     * Place new item at the end of the list
     *
     * @param string $value
     * @return LinkedListItem
     */
    public function appendList(string $value)
    {
        $item = new LinkedListItem($value);
        if ($this->last === null) {
            $this->setLast($item);
            return $item;
        }

        return $this->appendItem($this->last, $value);
    }

    /**
     * Insert item before $nextItem and maintain continuity
     *
     * @param LinkedListItem $nextItem
     * @param string         $value
     * @return LinkedListItem
     */
    public function prependItem(LinkedListItem $nextItem, string $value)
    {
        $item = new LinkedListItem($value);
        $item->setNext($nextItem);
        $item->setPrev($nextItem->getPrev());

        $nextItem->setPrev($item);

        if ( $item->getPrev() !== null )
            $item->getPrev()->setNext($item);

        if ( $this->first === $nextItem )
            $this->first = $item;

        return $item;
    }

    /**
     * Insert item after $prevItem and maintain continuity
     *
     * @param LinkedListItem $prevItem
     * @param string         $value
     * @return LinkedListItem
     */
    public function appendItem(LinkedListItem $prevItem, string $value)
    {
        $item = new LinkedListItem($value);
        $item->setPrev($prevItem);
        $item->setNext($prevItem->getNext());

        $prevItem->setNext($item);

        if ( $item->getNext() !== null )
            $item->getNext()->setPrev($item);

        if ( $this->last === $prevItem )
            $this->last = $item;

        return $item;
    }
}
