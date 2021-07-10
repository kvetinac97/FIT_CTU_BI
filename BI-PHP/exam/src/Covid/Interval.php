<?php

namespace FinalTask\Covid;

/**
 * Class Interval
 * @package FinalTask\Covid
 */
class Interval
{
    private string $from;
    private string $to;
    private int $sum;

    /**
     * Interval constructor.
     * @param string $from Date in YYYY-mm-dd format
     * @param string $to Date in YYYY-mm-dd format
     * @param int $sum Amount of tests performed in this interval
     */
    public function __construct(string $from, string $to, int $sum)
    {
        $this->from = $from;
        $this->to = $to;
        $this->sum = $sum;
    }

    public function getFrom(): string
    {
        return $this->from;
    }

    public function getTo(): string
    {
        return $this->to;
    }

    public function getSum(): int
    {
        return $this->sum;
    }

}
