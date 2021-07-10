package wrzecond.allergen;

import wrzecond.IReadDTO;

public class TjvAllergenReadDTO extends TjvAllergenDTO implements IReadDTO {

    public TjvAllergenReadDTO (int id, String name) {
        super (id, name);
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
