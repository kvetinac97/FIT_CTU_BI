package wrzecond.allergen;

import wrzecond.ICreateDTO;

public class TjvAllergenCreateDTO extends TjvAllergenDTO implements ICreateDTO {

    public TjvAllergenCreateDTO (String name) {
        super(null, name);
    }
    public TjvAllergenCreateDTO () { this(null); }

}
