<?php

namespace Iterator;

use Node;

abstract class AbstractOrderIterator implements \Iterator {

    /** @var Node[] $nodes */
    protected array $nodes = [];

    protected int $position = 0;

    public function __construct ( Node $root ) {
        $this->addNodes ( $root );
    }

    protected abstract function addNodes ( ?Node $node );

    public function current(): ?Node {
        return $this->nodes[$this->position];
    }

    public function next(): void {
        ++$this->position;
    }

    /**
     * @return bool|float|int|string|null
     */
    public function key() {
        return $this->position;
    }

    public function valid(): bool {
        return isset($this->nodes[$this->position]);
    }

    public function rewind(): void {
        $this->position = 0;
    }

}
