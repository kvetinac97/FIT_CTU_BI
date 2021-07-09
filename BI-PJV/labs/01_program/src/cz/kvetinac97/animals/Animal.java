package cz.kvetinac97.animals;

public abstract class Animal {

    protected String ANIMAL_NAME;

    public String getName() {
        return ANIMAL_NAME;
    }

    public abstract void move();

}