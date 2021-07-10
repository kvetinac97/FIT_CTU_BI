#!/usr/bin/env php
<?php

$contents = file_get_contents("php://stdin");
echo md5($contents) . "\n";
