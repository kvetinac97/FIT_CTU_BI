<?php

namespace Entity;

/**
 * @property int $id
 * @property string $name
 * @property float $price
 * @property float $another
 */
class TestEntity extends Entity {

    public function __construct ( array $data = [] ) {
        foreach ( $data as $key => $val )
            $this->{$key} = $val;
    }

}