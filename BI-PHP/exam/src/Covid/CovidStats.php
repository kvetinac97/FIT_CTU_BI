<?php

namespace FinalTask\Covid;

class CovidStats
{

    /**
     * Help function to sum all array values
     * @param array $field
     * @return int
     */
    private static function getSum ( array $field ) : int {
        return array_reduce($field, fn ($previous, $val) => $previous + $val[1], 0);
    }

    /**
     * Finds three 7-day intervals between given dates (including those dates), return instance of ThreeMostTestedIntervals with most performed tests
     * Each interval is 7 consecutive days eg. MON-SUN, TUE-MON, WED-TUE, THU-WED, FRI-THU, SAT-FRI or SUN-SAT
     * @param string $from Date in YYYY-mm-dd format
     * @param string $to Date in YYYY-mm-dd format
     * @return ThreeMostTestedIntervals
     * @throws \InvalidArgumentException
     */
    public static function getMostTestedIntervals(string $from, string $to): ThreeMostTestedIntervals
    {
        $fromTimestamp = strtotime($from); $toTimestamp = strtotime($to);
        if ( $toTimestamp - $fromTimestamp < 9*24*3600 )
            throw new \InvalidArgumentException("Invalid date span (needed at least 9 days)");

        if ( $fromTimestamp < strtotime('2020-01-27') || $toTimestamp > strtotime('2020-12-15') )
            throw new \InvalidArgumentException("Invalid from or to timestamp");

        $content = file_get_contents(__DIR__ . '/data/testy.json');
        $data = json_decode($content, true);

        $before = [];
        $count = 0;
        $intervals = [];

        foreach ( $data["data"] as $dayData ) {
            // Begin with loading
            if ( $count < 7 ) {
                $before[$count] = [ $dayData["datum"], $dayData["prirustkovy_pocet_testu"] ];
                $count++;

                // Set first interval
                if ($count == 7)
                    $intervals[] = new Interval($before[0][0], $before[6][0], self::getSum($before));
                continue;
            }

            // Add new day to interval (shift start and end)
            for ( $i = 0; $i < 6; $i++ )
                $before[$i] = $before[$i+1];
            $before[6] = [ $dayData["datum"], $dayData["prirustkovy_pocet_testu"] ];

            $intervals[] = new Interval($before[0][0], $before[6][0], self::getSum($before));
        }

        // Filter bad intervals
        $intervals = array_filter($intervals, fn ( Interval $value ) =>
            $fromTimestamp <= strtotime($value->getFrom()) && $toTimestamp >= strtotime($value->getTo())
        );

        // Sort by sum, descending
        usort($intervals, fn ( Interval $left, Interval $right ) =>
            ($right->getSum() - $left->getSum()) - ($left->getSum() - $right->getSum())
        );

        return new ThreeMostTestedIntervals($intervals[0], $intervals[1], $intervals[2]);
    }
}
