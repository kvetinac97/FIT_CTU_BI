<?php

namespace Iterator;

use Node;

class PostOrderIterator extends AbstractOrderIterator {

    protected function addNodes ( ?Node $node ) {
        if ( $node === null )
            return;

        $this->addNodes($node->getLeft());
        $this->addNodes($node->getRight());
        $this->nodes[] = $node;
    }

}
