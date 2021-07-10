<?php

use Entity\TestEntity;
use Repository\TestRepository;

require 'vendor/autoload.php';

$repo = new TestRepository();
$entity = new TestEntity([
    'id' => 1,
    'name' => 'Játrový knedlíček',
    'price' => 3.0,
    'another' => 521
]);

$repo->persist($entity);
$entity->id++;
$repo->persist($entity);

print_r($repo->findAll());
print_r($repo->getByID(3));
print_r($repo->getByID(2));