<?php

namespace FinalTask\Covid;

/**
 * Class ThreeMostTestedIntervals
 * @package FinalTask\Covid
 */
class ThreeMostTestedIntervals
{
    private Interval $first;
    private Interval $second;
    private Interval $third;

    public function __construct(Interval $first, Interval $second, Interval $third)
    {
        $this->first = $first;
        $this->second = $second;
        $this->third = $third;
    }

    public function getFirst(): Interval
    {
        return $this->first;
    }

    public function getSecond(): Interval
    {
        return $this->second;
    }

    public function getThird(): Interval
    {
        return $this->third;
    }
}
