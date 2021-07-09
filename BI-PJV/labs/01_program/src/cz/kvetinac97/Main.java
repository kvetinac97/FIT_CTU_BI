package cz.kvetinac97;

import cz.kvetinac97.animals.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Animal[] animals = new Animal[]{new Dog(), new Cat(), new Fish()};

        for (Animal animal : animals)
            animal.move();
    }

}
