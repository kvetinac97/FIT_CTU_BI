package cz.kvetinac97.product;

public abstract class Product {

    // Properties
    protected final String name;
    protected final int   price;
    protected int        amount;
    protected boolean guarantee;

    // Constructor
    public Product (String name, int price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.guarantee = false;
    }

    // Methods

    public void increaseAmount () {
        this.amount++;
    }

    public void decreaseAmount () {
        this.amount--;
    }

    @Override
    public String toString () {
        return this.name + " - " + this.price + " Kc " + this.amount + " ks";
    }

    // Getters

    public String getName () {
        return name;
    }

    public int getPrice () {
        return price;
    }

    public int getAmount () {
        return amount;
    }

    public boolean isGuarantee () {
        return guarantee;
    }

}
