package cz.kvetinac97.eshop;

import cz.kvetinac97.database.Database;
import cz.kvetinac97.product.Product;

public class EshopServiceImpl implements EshopService {

    // Properties
    private Database storage;

    @Override
    public void addProductToStorage (Product product) {
        this.storage.saveProduct(product);
    }

    @Override
    public void addProductsToStorage (Product... products) {
        for ( Product product : products )
            addProductToStorage(product);
    }

    @Override
    public boolean sellProduct (String name) {
        Product product;
        if ( ( product = storage.getProductByName(name) ) != null )
            storage.removeProduct(product);
        return product != null;
    }

    @Override
    public boolean returnProduct (String name) {
        return false;
    }

    @Override
    public void printStorage() {
        for ( Product product : storage.getProducts() )
            System.out.println("Product: " + product);
    }

}
