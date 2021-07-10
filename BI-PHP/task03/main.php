<?php

spl_autoload_register ( function($class) {
    if ( is_file("classes/$class.php") )
        require "classes/$class.php";
} );

// Bag tests

$bag = new Bag;
assert($bag->isEmpty() === true);
assert($bag->contains('celery') === false);
assert($bag->elementSize('celery') === 0);
assert($bag->size() === 0);

$bag->add('celery');
assert($bag->isEmpty() === false);
assert($bag->contains('celery') === true);
assert($bag->elementSize('celery') === 1);
assert($bag->size() === 1);

$bag->add('celery');
$bag->add('celery');
$bag->add('celery');
assert($bag->elementSize('celery') === 4);
assert($bag->size() === 4);

$bag->add('eggplant');
$bag->add('eggplant');
assert($bag->elementSize('celery') === 4);
assert($bag->elementSize('eggplant') === 2);
assert($bag->size() === 6);

$bag->add('parsley');
assert($bag->elementSize('celery') === 4);
assert($bag->elementSize('eggplant') === 2);
assert($bag->elementSize('parsley') === 1);
assert($bag->size() === 7);

$bag->remove('parsley');
assert($bag->elementSize('celery') === 4);
assert($bag->elementSize('eggplant') === 2);
assert($bag->elementSize('parsley') === 0);
assert($bag->size() === 6);

$bag->remove('celery');
assert($bag->elementSize('celery') === 3);
assert($bag->elementSize('eggplant') === 2);
assert($bag->elementSize('parsley') === 0);
assert($bag->size() === 5);

$bag->remove('celery');
$bag->remove('celery');
$bag->remove('celery');
$bag->remove('celery');
$bag->remove('celery');
$bag->remove('celery');
assert($bag->elementSize('celery') === 0);
assert($bag->elementSize('eggplant') === 2);
assert($bag->elementSize('parsley') === 0);
assert($bag->size() === 2);

$bag->clear();
assert($bag->elementSize('celery') === 0);
assert($bag->elementSize('eggplant') === 0);
assert($bag->elementSize('parsley') === 0);
assert($bag->size() === 0);


// Set tests
$set = new Set;
assert($set instanceof Bag);
assert($set->isEmpty() === true);
assert($set->contains('celery') === false);
assert($set->elementSize('celery') === 0);
assert($set->size() === 0);

$set->add('celery');
assert($set->isEmpty() === false);
assert($set->contains('celery') === true);
assert($set->elementSize('celery') === 1);
assert($set->size() === 1);

$set->add('celery');
$set->add('celery');
$set->add('celery');
assert($set->elementSize('celery') === 1);
assert($set->size() === 1);

$set->add('eggplant');
$set->add('eggplant');
assert($set->elementSize('celery') === 1);
assert($set->elementSize('eggplant') === 1);
assert($set->size() === 2);

$set->add('parsley');
assert($set->elementSize('celery') === 1);
assert($set->elementSize('eggplant') === 1);
assert($set->elementSize('parsley') === 1);
assert($set->size() === 3);

$set->remove('parsley');
assert($set->elementSize('celery') === 1);
assert($set->elementSize('eggplant') === 1);
assert($set->elementSize('parsley') === 0);
assert($set->size() === 2);

$set->remove('celery');
assert($set->elementSize('celery') === 0);
assert($set->elementSize('eggplant') === 1);
assert($set->elementSize('parsley') === 0);
assert($set->size() === 1);

$set->remove('celery');
$set->remove('celery');
$set->remove('celery');
$set->remove('celery');
$set->remove('celery');
$set->remove('celery');
assert($set->elementSize('celery') === 0);
assert($set->elementSize('eggplant') === 1);
assert($set->elementSize('parsley') === 0);
assert($set->size() === 1);

$set->clear();
assert($set->elementSize('celery') === 0);
assert($set->elementSize('eggplant') === 0);
assert($set->elementSize('parsley') === 0);
assert($set->size() === 0);
