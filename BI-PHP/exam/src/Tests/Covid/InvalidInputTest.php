<?php

namespace FinalTask\Tests\Covid;

use FinalTask\Covid\CovidStats;
use PHPUnit\Framework\TestCase;

class InvalidInputTest extends TestCase
{
    public function invalidInputProvider() {
        yield ['2020-01-01', '2020-02-15'];
        yield ['2020-03-01', '2020-02-15'];
        yield ['2020-03-01', '2020-03-01'];
        yield ['2020-03-01', '2020-03-07'];
        yield ['2020-03-01', '2020-03-08'];
        yield ['2021-01-01', '2020-01-20'];
    }

    /**
     * @dataProvider invalidInputProvider
     * @throws \Exception
     */
    public function testInvalidInput($from, $to) {
        $this->expectException(\InvalidArgumentException::class);
        CovidStats::getMostTestedIntervals($from, $to);
    }
}
