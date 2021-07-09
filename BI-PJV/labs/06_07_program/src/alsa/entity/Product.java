package alsa.entity;

import java.io.Serializable;
import java.util.Objects;

public abstract class Product implements Serializable {

    protected final int id;

    protected final String name;

    protected final double price;

    protected final int count;

    public Product(int id, String name, double price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public double price() {
        return price;
    }

    public int count() {
        return count;
    }

    public abstract Product increaseCount();

    public abstract Product decreaseCount();

    public abstract boolean hasSpecialGuarantee();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Double.compare(product.price, price) == 0 &&
                count == product.count &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, count);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
