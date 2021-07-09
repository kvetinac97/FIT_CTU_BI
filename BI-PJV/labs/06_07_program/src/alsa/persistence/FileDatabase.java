package alsa.persistence;

import alsa.entity.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileDatabase implements Database {

    @Override
    public List<Product> findProducts() {
        return loadProducts();
    }

    @Override
    public Optional<Product> findProductById(Integer id) {
        return Optional.ofNullable(loadProducts().get(id));
    }

    @Override
    public void saveProduct(Product product) {
        List<Product> products = loadProducts();
        products.add(product);
        persistProducts(products);
    }

    @Override
    public void removeProduct(Product product) {
        List<Product> products = loadProducts();
        products.remove(product);
        persistProducts(products);
    }

    private void persistProducts ( List<Product> products ) {

        try ( FileOutputStream os = new FileOutputStream("alsa.db");
              ObjectOutputStream oos = new ObjectOutputStream(os) ) {

            oos.writeObject(products); // Java to zvládne sama převést
            oos.flush();

        }
        catch ( IOException exc ) {
            exc.printStackTrace();
        }

    }

    private List<Product> loadProducts () {

        try ( FileInputStream os = new FileInputStream("alsa.db");
              ObjectInputStream oos = new ObjectInputStream(os) ) {

            List<Product> products = (List<Product>) oos.readObject(); // Java to zvládne sama převést
            return products != null ? products : new ArrayList<>();

        }
        catch ( EOFException exc ) {
            System.out.println("Empty file");
        }
        catch ( ClassNotFoundException | IOException exc ) {
            System.out.println("File not found");
        }

        return new ArrayList<>();

    }

}
