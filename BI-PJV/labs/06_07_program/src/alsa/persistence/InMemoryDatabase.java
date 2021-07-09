package alsa.persistence;

import alsa.entity.Product;

import java.util.*;

public class InMemoryDatabase implements Database {

    private final Map<Integer, Product> products = new HashMap<>();

    public List<Product> findProducts() {
        return new ArrayList<>(products.values());
    }

    public Optional<Product> findProductById(Integer id) {
        Product product = products.get(id);
        return Optional.ofNullable(product);
    }

    public void saveProduct(Product product) {
        products.put(product.id(), product);
    }

    public void removeProduct(Product product) {
        products.remove(product.id());
    }
}
