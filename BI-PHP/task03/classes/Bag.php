<?php

class Bag {

    /** @var int[] $collection */
    private $collection = [];

    public function add($item) {
        if ( $this->contains($item) )
            $this->collection[$item]++;
        else
            $this->collection[$item] = 1;
    }

    public function clear() {
        $this->collection = [];
    }

    public function contains($item) {
        return isset($this->collection[$item]);
    }

    public function elementSize($item) {
        return $this->contains($item) ? $this->collection[$item] : 0;
    }

    public function isEmpty() {
        return empty($this->collection);
    }

    public function remove($item) {
        if ( !$this->contains($item) )
            return;

        if ( $this->elementSize($item) > 1 )
            $this->collection[$item]--;
        else
            unset($this->collection[$item]);
    }

    public function size() {
        return array_sum($this->collection);
    }

}
