<?php

namespace FinalTask\Tests\Triangle;

use FinalTask\Triangle\Triangle;
use PHPUnit\Framework\TestCase;

class TriangleSimilarTest extends TestCase
{
    use TriangleTrait;

    /**
     * @throws \Exception
     */
    public function testRandomSimilar()
    {
        $a = random_int(1, 100);
        $b = random_int(1, 100);
        $c = $this->getC($a, $b);
        $k = random_int(1, 100) / 100;

        $t = new Triangle($a, $b, $c);
        $t1 = new Triangle($a * $k, $b * $k, $c * $k);
        $t2 = new Triangle($b * $k, $c * $k, $a * $k);
        $t3 = new Triangle($c * $k, $a * $k, $b * $k);

        $this->assertTrue(Triangle::isSimilar($t, $t1),
            "[$a, $b, $c] should be similar to [{$t1->getA()},{$t1->getB()},{$t1->getC()}]"
        );

        $this->assertTrue(Triangle::isSimilar($t, $t2),
            "[$a, $b, $c] should be similar to [{$t2->getA()},{$t2->getB()},{$t2->getC()}]"
        );

        $this->assertTrue(Triangle::isSimilar($t, $t3),
            "[$a, $b, $c] should be similar to [{$t3->getA()},{$t3->getB()},{$t3->getC()}]"
        );
    }

    /**
     * @dataProvider similarData
     * @throws \Exception
     */
    public function testSimilar($a1, $b1, $c1, $a2, $b2, $c2)
    {
        $t1 = new Triangle($a1, $b1, $c1);
        $t2 = new Triangle($a2, $b2, $c2);

        $this->assertTrue(Triangle::isSimilar($t1, $t2));
    }

    public function similarData()
    {
        return [
            [10, 20, 30, 2, 4, 6],
            [10, 20, 30, 60, 180, 120],
            [3, 4, 5, 4, 3, 5],
            [0, 0, 0, 0, 0, 0],
            [13, 17, 19, 59.5, 66.5, 45.5],
            [3, 4, 5, 3, 4, 5],
        ];
    }
}
