package cz.kvetinac97.eshop;

import cz.kvetinac97.product.Product;

public interface EshopService {

    // Add products
    void addProductToStorage  (Product product);
    void addProductsToStorage (Product... products);

    // Remove products
    boolean sellProduct       (String name);
    boolean returnProduct     (String name);

    // Show storage
    void printStorage();

}
