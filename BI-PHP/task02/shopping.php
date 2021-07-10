<?php

function getPrice($item) : float {
    // preg_match returns number of matches, which must be 1 successful match
    if ( preg_match("/(?:CZK ?([0-9,.]+)|([0-9,.]+) ?(?:Kč|CZK|,-))/", $item, $matches) !== 1 )
        return 0;

    // Get rid of thousand points and commas and select the correct match
    $price = str_replace( [".", ","], ["", "."], !empty($matches[1]) ? $matches[1] : $matches[2] );
    return floatval($price);
}

function sortList($list) {
	usort($list, function ($first, $second) {
	    // Amount will always be >0 so this function will be correct
	    return ceil(getPrice($first) - getPrice($second));
    });
	return $list;
}

function sumList($list) {
    // Map lines to amounts and summarize them
	return array_sum( array_map(function ($map) {
	    return getPrice($map);
    }, $list) );
}

if (count($argv) !== 2) {
	echo "Usage: php shopping.php <input>\n";
	exit(1);
}
$input = file_get_contents(end($argv));
$list = explode(PHP_EOL, $input);
$list = sortList($list);
print_r($list);
print_r(sumList($list) . PHP_EOL);
