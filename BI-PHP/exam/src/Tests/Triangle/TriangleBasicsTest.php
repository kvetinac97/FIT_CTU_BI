<?php

namespace FinalTask\Tests\Triangle;

use FinalTask\Triangle\Triangle;
use PHPUnit\Framework\TestCase;

class TriangleBasicsTest extends TestCase
{
    use TriangleTrait;

    /**
     * @throws \Exception
     */
    public function testGetters()
    {
        $a = random_int(1, 100);
        $b = random_int(1, 100);
        $c = $this->getC($a, $b);

        $t = new Triangle($a, $b, $c);

        $this->assertEquals($a, $t->getA());
        $this->assertEquals($b, $t->getB());
        $this->assertEquals($c, $t->getC());
    }

    /**
     * @dataProvider invalidValuesData
     */
    public function testInvalidValues($a, $b, $c)
    {
        $this->expectException(\Exception::class);

        $t = new Triangle($a, $b, $c);
    }

    public function invalidValuesData()
    {
        return [
            [-5, 3, 4],
            [10, -7, 4],
            [7, 8, -9],
            [1, 2, 4],
            [90, 20, 15],
            [13, 70, 8],
        ];
    }

    /**
     * @dataProvider zeroValuesData
     */
    public function testZeroValues($a, $b, $c)
    {
        $t = new Triangle($a, $b, $c);

        $this->assertTrue($t->getA() == 0 || $t->getB() == 0 || $t->getC() == 0);
    }

    public function zeroValuesData()
    {
        return [
            [0, 4, 4],
            [8, 0, 8],
            [13, 13, 0]
        ];
    }
}
