<?php

namespace kvetinac97\Product;

use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\GreaterThanOrEqual;
use Symfony\Component\Validator\Constraints\PositiveOrZero;
use Symfony\Component\Validator\Validation;

class Product {

    /** @var float $price */
    private $price;

    /**
     * @Assert\PositiveOrZero()
     * @Assert\LessThanOrEqual(100)
     */
    private $vat;

    // === SETTERS ===
    public function setPrice ( float $price ) : void {
        // Použití validátoru - ne příliš efektivní
        $validator = Validation::createValidator();
        $violations = $validator->validate($price, [
            new PositiveOrZero(),
            new GreaterThanOrEqual(0)
        ]);

        if ( $violations->count() > 0 ) {
            foreach ( $violations as $violation )
                echo "Chyba: {$violation->getMessage()} \n";
            return;
        }

        echo "Set price successfull \n";
        $this->price = $price;
        $this->print();
    }
    public function setVAT ( float $vat ) : void {
        echo "Set VAT successfull\n";
        $this->vat = $vat;
        $this->print();
    }

    public function print () : void {
        echo "{$this->price} {$this->vat} \n";
    }

}