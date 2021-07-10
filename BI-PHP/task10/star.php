#!/usr/bin/env php
<?php

use Star\StarCommand;
use Symfony\Component\Console\Application;

require __DIR__ . '/vendor/autoload.php';


$application = new Application();
$command = new StarCommand();

$application->add($command);

$application->setDefaultCommand($command->getName(), true);
$application->run();
