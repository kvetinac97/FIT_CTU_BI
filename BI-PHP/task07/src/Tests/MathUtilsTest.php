<?php

namespace HW\Tests;

use HW\Lib\MathUtils;
use PHPUnit\Framework\TestCase;

/**
 * @covers \HW\Lib\MathUtils
 */
class MathUtilsTest extends TestCase
{

    // Constant for base data
    private const RANDOM_DATA_COUNT = 30;

    /**
     * Basic test of sum function
     * @param int[] $array
     * @param int $expectedResult
     * @dataProvider sumBasic
     */
    public function testSumBasic (array $array, int $expectedResult) {
        $this->assertSame($expectedResult, MathUtils::sum($array));
    }
    public function sumBasic () : array {
        return [
            [ [], 0 ],
            [ [-4], -4 ],
            [ [128, -63, -66, 1], 0 ],
            [ [1, 2, 3, 4], 10 ],
        ];
    }

    /**
     * Random test of sum function
     * @param int[] $array
     * @param int $expectedResult
     * @dataProvider sumRandom
     */
    public function testSumRandom (array $array, int $expectedResult) {
        $this->assertSame($expectedResult, MathUtils::sum($array));
    }
    public function sumRandom () : \Generator {
        $ex = self::RANDOM_DATA_COUNT;
        while ( $ex-- > 0 ) {
            $countNum = mt_rand(1, 25);
            $f = [];
            while ( $countNum-- > 0 )
                $f[] = mt_rand(-999999999, 999999999);
            yield [ $f, array_sum($f) ];
        }
    }

    /**
     * Basic test of linear equation
     * @param int|float $a
     * @param int|float $b
     * @param int|float $expectedResult
     * @dataProvider linearBasic
     */
    public function testSolveLinearBasic ($a, $b, $expectedResult) {
        $this->assertEquals($expectedResult, MathUtils::solveLinear($a, $b));
    }
    public function linearBasic () : array {
        return [
            [ 1, -5, 5 ],
            [ 1, 3, -3 ],
            [ 2, -12, 6 ],
            [ 3, 6, -2 ],
            [ 0.5, -4, 8 ],
            [ 0.125, 0, 0 ]
        ];
    }

    public function testSolveLinearInvalid () {
        $this->expectException(\InvalidArgumentException::class);
        MathUtils::solveLinear(0, 0); // 0x + O = 0
    }

    /**
     * Random test of linear equation
     * @param int|float $a
     * @param int|float $b
     * @dataProvider linearRandom
     */
    public function testSolveLinearRandom ($a, $b) {
        $this->assertEqualsWithDelta(0, $a * MathUtils::solveLinear($a, $b) + $b, 0.000001);
    }
    public function linearRandom () : \Generator {
        // Random data
        $ex = self::RANDOM_DATA_COUNT;
        while ( $ex-- > 0 ) {
            $a = mt_rand(1, 2) == 1 ? mt_rand(-999999999, -1) : mt_rand(1, 999999999);
            $b = mt_rand(1, 2) == 1 ? mt_rand(-999999999, -1) : mt_rand(1, 999999999);
            yield [ $a, $b ];
        }
    }

    /**
     * Basic test of quadratic equation
     * @param int|float $a
     * @param int|float $b
     * @param int|float $c
     * @param int[]|float[] $expectedResult
     * @dataProvider quadraticBasic
     */
    public function testSolveQuadraticBasic ($a, $b, $c, array $expectedResult) {
        $result = MathUtils::solveQuadratic($a, $b, $c);
        $this->assertCount(count($expectedResult), $result);
        foreach ( $expectedResult as $expectedVal )
            $this->assertContainsEquals($expectedVal, $result);
    }
    public function quadraticBasic () : array {
        return [
            [ 1, 0, -4, [2, -2] ], // x^2 - 4 = 0
            [ 1, 2, 1, [-1] ], // x^2 + 2x + 1 = 0
            [ 1, 0, 1, [] ], // x^2 + 1 = 0
            [ 2, 18, 36, [-3, -6] ] // 2x^2 + 18x + 36 = 0
        ];
    }

    public function testSolveQuadraticInvalid () {
        $this->expectException(\InvalidArgumentException::class);
        MathUtils::solveQuadratic(0, 1, 1);
    }

}
