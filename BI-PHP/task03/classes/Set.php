<?php

class Set extends Bag {

    public function add($item) {
        if ( $this->contains($item) )
            return;
        parent::add($item);
    }

}
