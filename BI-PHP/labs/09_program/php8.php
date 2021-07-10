<?php

class php8 {

    public function __construct (
        private int|float $x
    ) {}

    public function getIntFloat () : int|float {
        return $this->x;
    }
}

function test ($optional = "i am ignored", ?php8 $test = null) : int|null {
    echo $optional . PHP_EOL;
    return $test?->getIntFloat();
}

echo "Fst " . test(test: new php8(x: 5.2)) . PHP_EOL;
echo "Snd " . test(null) . PHP_EOL;

echo match (null) {
    '0' => "Nechci",
    null => "Chci",
    default => "Def"
} . PHP_EOL;