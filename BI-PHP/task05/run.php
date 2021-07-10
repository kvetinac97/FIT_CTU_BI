<?php declare(strict_types=1);

use App\Builder;
use App\Renderer;

/*
 * usage:
 *
 * php run.php > invoice.pdf
 *
 */

require 'vendor/autoload.php';

$builder = new Builder();
$invoice = $builder
    ->setNumber('201900021')
    ->setSupplier(
        'Dodavatel Holding a.s.', 'CZ66776677', 'Kratka', "2", 'Brno', '60200', '+420 999 100 999', 'info@d-holding.eu'
    )
    ->setCustomer('Jan Novak', 'CZ12345678', 'Dlouha', "1", 'Praha', '11000', '+420 977 101 202')
    ->addItem('Zbozi', 15.5, 199.99)
    ->addItem('Sluzby', 1, 98100.57)
    ->build();

echo (new Renderer)->makeInvoice($invoice);
