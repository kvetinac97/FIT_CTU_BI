package cz.kvetinac97.database;

import cz.kvetinac97.product.Product;

public interface Database {

    // Getter
    Product[] getProducts ();
    Product getProductByName (String name);

    // Setter
    void saveProduct   (Product product);
    void removeProduct (Product product);

}
