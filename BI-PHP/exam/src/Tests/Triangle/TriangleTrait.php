<?php

namespace FinalTask\Tests\Triangle;

use FinalTask\Triangle\Triangle;

trait TriangleTrait
{
    /**
     * @param $a
     * @param $b
     * @return int
     * @throws \Exception
     */
    protected function getC($a, $b)
    {
        return random_int(abs($a - $b) + Triangle::EPSILON, $a + $b - Triangle::EPSILON);
    }
}
