<?php

namespace Iterator;

use Node;

class PreOrderIterator extends AbstractOrderIterator {

    protected function addNodes ( ?Node $node ) {
        if ( $node === null )
            return;

        $this->nodes[] = $node;
        $this->addNodes($node->getLeft());
        $this->addNodes($node->getRight());
    }

}
