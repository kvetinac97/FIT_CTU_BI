package wrzecond.food;

import wrzecond.IReadDTO;

import java.util.Collections;
import java.util.List;

public class TjvFoodReadDTO extends TjvFoodDTO implements IReadDTO {

    public TjvFoodReadDTO (Integer id, String name, Integer price, Boolean cooked, List<Integer> allergens) {
        super (id, name, price, cooked, allergens == null ? Collections.emptyList() : allergens);
    }

    @Override
    public int getId () {
        return id;
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

}
