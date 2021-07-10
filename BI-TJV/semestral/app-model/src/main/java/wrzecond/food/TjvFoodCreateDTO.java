package wrzecond.food;

import wrzecond.ICreateDTO;

import java.util.Collections;
import java.util.List;

public class TjvFoodCreateDTO extends TjvFoodDTO implements ICreateDTO {

    public TjvFoodCreateDTO (String name, Integer price, Boolean cooked, List<Integer> allergens) {
        super (null, name, price, cooked, allergens == null ? Collections.emptyList() : allergens);
    }
    public TjvFoodCreateDTO () {
        this (null, null, null, null);
    }

}
