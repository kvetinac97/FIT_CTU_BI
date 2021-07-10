<?php declare(strict_types=1);

function randomArray ( int $length, int $min = 0, int $max = 100 ) {
    $array = [];
    for ( $i = 0; $i < $length; $i++ )
        $array[$i] = mt_rand($min, $max);
    return $array;
}

print_r($p = randomArray(10));

function customSort(array &$pole) {
    usort($pole, function($a, $b) { return $b - $a; } );
}

customSort($p);
print_r($p);

/**
 * Napište funkci, která má proměnný počet argumentů a vrací
 * aritmetický průměr a medián z numerických hodnot .
 */
function meanAndMedian(...$args) {
    $sum = 0; $count = count($args); sort($args);
    foreach ( $args as $arg ) {
        $sum += $arg;
        $freq[$arg] = isset($freq[$arg]) ? $freq[$arg] + 1 : 1;
    }
    return [
        'mean' => $sum/$count,
        'median' => $args[$count / 2]
    ];
}

print_r(meanAndMedian(75, 4, 2, 3, 2, 5, 1));


function errorHandler(...$params)
{
    file_put_contents(date('Y-m-d H:i:s') . '.txt', print_r($params, true));
}

set_error_handler('errorHandler');
$t = UNDEFINED_CONSTANT;

function isEmail($mail)
{
    return preg_match("/.*@.*\..*/", $mail);
}

print_r((isEmail("bivoj@example.com") ? "Y" : "N"). PHP_EOL);

function isMacAddress($address)
{
    $b = preg_match_all("/^([0-9a-fA-F]{2}):([0-9a-fA-F]{2}):([0-9a-fA-F]{2}):([0-9a-fA-F]{2}):([0-9a-fA-F]{2}):([0-9a-fA-F]{2})$/", $address, $matches);
    return !$b ? null : [
        $matches[1][0], $matches[2][0], $matches[3][0], $matches[4][0], $matches[5][0], $matches[6][0]
    ];
}

print_r((isMacAddress("a0:1b:c2:3d:e4:5f") ?? "N\n"));
print_r((isMacAddress("a0:1b:c2:3d:e4:5ff") ?? "N\n"));
