<?php

spl_autoload_register( function($class) {
    $class = str_replace("\\", "/", $class);
    if ( is_file("src/$class.php") )
        require "src/$class.php";
} );






// Don't do anything with the following code, it is just for testing your solution.


function treeToString($iterator) {
    $values = [];
    foreach ($iterator as $node) {
        $values[] = $node->getValue();
    }
    return implode(' ', $values);
}

function printTreeResult($label, $result) {
    echo "  - $label " . ($result ? '✓' : '×') . "\n";
}

function testTrees($order, $getIterator) {
    $result = true;
    for ($i = 1; $i <= TREE_COUNT; $i++) {
        $tree = "tree$i";
        $expected = "tree$i$order";
        global $$tree;
        global $$expected;
        $testResult = treeToString($getIterator($$tree)) === $$expected;
        printTreeResult("$tree ($order-order)", $testResult);
        $result &= $testResult;
    }
    return $result;
}

require_once('./trees.php');


// Test autoloader
echo "*> Autoloader ...\n";
$classes = ['\\Node', 'Iterator\\InOrderIterator', 'Iterator\\PreOrderIterator', 'Iterator\\PostOrderIterator'];
$allExists = array_reduce($classes, function($acc, $class) {
    return $acc && class_exists($class, true);
}, true);
echo '=> ' . ($allExists ? 'OK [1]' : 'FAILED') . "\n";



// Test in-order iterator
echo "\n*> In-Order Iterator ...\n";
$inOrderOk = testTrees('in', function($n) { return new Iterator\InOrderIterator($n); });
echo '=> '. ($inOrderOk ? 'OK [2]' : 'FAILED') . "\n";


// Test pre- and post-order iterators
echo "\n*> Pre- and Post-Order Iterators ...\n";
$preOrderOk = testTrees('pre', function($n) { return new Iterator\PreOrderIterator($n); });
$postOrderOk = testTrees('post', function($n) { return new Iterator\PostOrderIterator($n); });
echo '=> '. ($preOrderOk && $postOrderOk ? 'OK [2]' : 'FAILED') . "\n";


// Test IteratorAggregate
echo "\n*> IteratorAggregate ...\n";
$iteratorAggregateOk = testTrees('in', function($n) { return $n; });
echo '=> '. ($iteratorAggregateOk ? 'OK [2]' : 'FAILED') . "\n";
