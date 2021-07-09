package cz.kvetinac97.entity;

import cz.kvetinac97.Category;

/**
 * Notebook class
 * @author wrzecond
 */
public class Notebook {

    private String       name;
    private int         price;
    private Category category;

    public Notebook (String name, int price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName () {
        return name;
    }

    public int getPrice () {
        return price;
    }

    public Category getCategory () {
        return category;
    }

    public Notebook setPrice (int price) {
        this.price = price;
        return this;
    }

}