<?php

namespace FinalTask\Tests\Covid;

use FinalTask\Covid\CovidStats;
use FinalTask\Covid\Interval;
use PHPUnit\Framework\TestCase;

class CorrectInputTest extends TestCase
{
    public function correctInputProvider() {
        yield ['2020-03-01', '2020-05-31', [ // spring
            'getFirst' => new Interval('2020-04-16', '2020-04-22', 50027),
            'getSecond' => new Interval('2020-04-15', '2020-04-21', 49685),
            'getThird' => new Interval('2020-04-17', '2020-04-23', 49547)
        ]];
        yield ['2020-06-01', '2020-08-31', [ // summer
            'getFirst' => new Interval('2020-08-25', '2020-08-31', 57562),
            'getSecond' => new Interval('2020-08-24', '2020-08-30', 55604),
            'getThird' => new Interval('2020-08-23', '2020-08-29', 54062)
        ]];
        yield ['2020-09-01', '2020-12-15', [ // fall (incomplete)
            'getFirst' => new Interval('2020-10-21', '2020-10-27', 280780),
            'getSecond' => new Interval('2020-10-22', '2020-10-28', 275641),
            'getThird' => new Interval('2020-10-20', '2020-10-26', 273879)
        ]];
        yield ['2020-12-01', '2020-12-15', [ // just first half of December
            'getFirst' => new Interval('2020-12-09', '2020-12-15', 158565),
            'getSecond' => new Interval('2020-12-08', '2020-12-14', 155178),
            'getThird' => new Interval('2020-12-07', '2020-12-13', 152965)
        ]];
    }

    /**
     * @dataProvider correctInputProvider
     */
    public function testCorrectInput($from, $to, $intervals) {
        $result = CovidStats::getMostTestedIntervals($from, $to);

        foreach ($intervals as $index => $correct) {
            foreach (['getFrom', 'getTo', 'getSum'] as $value) {
                $this->assertSame($correct->$value(), $result->$index()->$value());
            }
        }
    }
}
