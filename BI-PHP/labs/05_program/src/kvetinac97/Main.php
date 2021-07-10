<?php

namespace kvetinac97;

use Monolog\Handler\StreamHandler;
use Monolog\Logger;

class Main {

    public function __construct() {
        echo "Main!\n";

        $product = new Product\Product();
        $product->setPrice(35);
        $product->setPrice(-100);
        $product->setPrice(-0.543);

        $cvalidator = new CValidator();
        $product->setVAT(-15);
        $cvalidator->validate($product);

        $product->setVAT(100.5);
        $cvalidator->validate($product);

        $logger = new Logger('channel1');
        $logger->pushHandler(new StreamHandler(__DIR__ . '/../../eshop.log'));
        $logger->log('error', 'Hello');

    }

}