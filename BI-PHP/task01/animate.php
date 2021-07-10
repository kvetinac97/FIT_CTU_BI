<?php

require_once './game.php';

if (count($argv) !== 2) {
    echo "Usage: php animate.php <input>\n";
    exit(1);
}


function clearTerminal () {
  if (strncasecmp(PHP_OS, 'win', 3) === 0) {
    popen('cls', 'w');
  } else {
    system('clear');
  }
}


$input = file_get_contents(end($argv));
$next = readInput($input);



while(true) {
    clearTerminal();
    echo(writeOutput($next));
    echo "\n";

    usleep(500000);
    $next = gameStep($next);

}
