package alsa.comparator;

import alsa.entity.Product;

import java.util.Comparator;

public class ProductIdComparator implements Comparator<Product> {

    private final SortOrder sortOrder;

    public ProductIdComparator(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int compare(Product o1, Product o2) {
        int result = Integer.compare(o1.id(), o2.id());
        return sortOrder == SortOrder.ASCENDING ? result : -result;
    }

}
