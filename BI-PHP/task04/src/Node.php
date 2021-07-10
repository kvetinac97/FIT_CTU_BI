<?php

use Iterator\InOrderIterator;

class Node implements IteratorAggregate
{
    /**
     * @var int
     */
    protected $value;

    /**
     * @var Node|null
     */
    protected $left;

    /**
     * @var Node|null
     */
    protected $right;

    /**
     * Node constructor.
     *
     * @param int $value
     */
    public function __construct(int $value)
    {
        $this->value = $value;
    }

    /**
     * @return int
     */
    public function getValue(): int
    {
        return $this->value;
    }

    /**
     * @return Node|null
     */
    public function getLeft(): ?Node
    {
        return $this->left;
    }

    /**
     * @param Node|null $left
     * @return Node
     */
    public function setLeft(?Node $left): Node
    {
        $this->left = $left;

        return $this;
    }

    /**
     * @return Node|null
     */
    public function getRight(): ?Node
    {
        return $this->right;
    }

    /**
     * @param Node|null $right
     * @return Node
     */
    public function setRight(?Node $right): Node
    {
        $this->right = $right;

        return $this;
    }

    public function getIterator() {
        return new InOrderIterator($this);
    }

}
