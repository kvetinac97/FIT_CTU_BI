import java.lang.reflect.Type;
import java.util.*;

public class Main {

    public static void main ( String[] args ) {
        Product[] pr = new Product[]{
                new Product("Lenovo KB", 25000),
                new Product("Dell XPS", 40000),
                new Product("Stará šunka ze smetiště", 5)
        };

        List<Product> products = new ArrayList<>(List.of(pr));
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare ( Product product1, Product product2 ) {
                // 0 - stejné, > 0 první < druhý, < 0 první > druhý
                return product1.name.compareTo(product2.name);
            }
        });

        products.forEach(System.out::println);

        Optional<Product> product = findByName(products, "Dell XPS");
        if ( product.isPresent() )
            System.out.println(product);

        Optional<Product> product2 = findByName(products, "Dell XS");
        if ( product2.isEmpty() )
            System.out.println("Not found");
    }

    public static Optional<Product> findByName (List<Product> products, String name) {
        for ( Product product : products )
            if ( Objects.equals( product.name, name ) )
                return Optional.of(product);

        return Optional.empty();
    }

}
