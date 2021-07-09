package cz.kvetinac97.product;

import cz.kvetinac97.product.computer.ComputerPart;

public class ProductNotebook extends Product {

    protected ProductNotebookCategory category;
    protected ComputerPart[] parts;

    public ProductNotebook (String name, int price, int amount,
                            ProductNotebookCategory category,
                            ComputerPart[] parts) {
        super(name, price, amount);
        this.category = category;
        this.parts = parts;
    }

}
