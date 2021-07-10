<?php

$i = "10"; $s = 8; $t = '1e1';
var_dump([$i, $s, $t]);

$a = 5; $b = 6; $c = 'b';
echo $a + $$c . "\n";

$a = 123e1;
$b = '123e1x56';
echo intval($a == $b) . "\n";   // ?
echo intval($a === $b) . "\n";  // ?

// Swap two vars
function swap ( &$a, &$b ) {
    $a = [$a, $b];
    $b = $a[0];
    $a = $a[1];

    // Shorter solution
    // [$a, $b] = [$b, $a];
}

$t = 1; $u = 5;
swap($t, $u);
echo "$t $u \n";

// Fibonacci
function fb ( $target, $first = 0, $second = 1, $n = 2 ) {
    if ( $n <= 0 )
        return 0;
    if ( $n == 1 )
        return 1;
    if ( $n-1 == $target )
        return $first + $second;
    return fb ( $target, $first + $second, $first, $n+1 );
}

echo fb(32) . "\n";