<?php

namespace Iterator;

use Node;

class InOrderIterator extends AbstractOrderIterator {

    protected function addNodes ( ?Node $node ) {
        if ( $node === null )
            return;

        $this->addNodes($node->getLeft());
        $this->nodes[] = $node;
        $this->addNodes($node->getRight());
    }

}
