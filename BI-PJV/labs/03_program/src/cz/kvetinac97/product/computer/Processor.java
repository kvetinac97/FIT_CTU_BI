package cz.kvetinac97.product.computer;

import cz.kvetinac97.product.Product;

public class Processor implements ComputerPart {

    private ProcessorSpeed speed;

    public Processor ( ProcessorSpeed speed ) {
        this.speed = speed;
    }

}
