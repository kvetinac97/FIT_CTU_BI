<?php

namespace App;

class MathClass {

    public function add ( int $left, int $right ) : int {
        return $left + $right;
    }

    public function sayHi () {
        echo "Hello!" . PHP_EOL;
    }

}