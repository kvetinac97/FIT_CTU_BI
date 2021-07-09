import java.util.Objects;

public class Product implements Comparable<Product> {

    public String name;
    public int   price;

    public Product ( String name, int price ) {
        this.name = name;
        this.price = price;
    }

    @Override
    public boolean equals ( Object object ) {
        if ( !( object instanceof Product ) )
            return false;

        Product product = (Product) object;
        return price == product.price && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "Product{" + "name='" + name + '\'' + ", price=" + price + '}';
    }

    @Override
    public int compareTo (Product product) {
        return product.price - price;
    }

}
