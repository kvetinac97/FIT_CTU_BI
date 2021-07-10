<?php

trait Magic {
    function howdy() { echo "How-dy!" . PHP_EOL; }
}

class TraitUser {
    use Magic;
}

( new TraitUser() )->howdy();