<?php

require_once './game.php';

if (count($argv) !== 2) {
    echo "Usage: php next.php <input>\n";
    exit(1);
}


$input = file_get_contents(end($argv));

$array = readInput($input);
$next = gameStep($array);
$output = writeOutput($next);

echo($output);
echo "\n";
