package wrzecond.table;

import wrzecond.ICreateDTO;

public class TjvTableCreateDTO extends TjvTableDTO implements ICreateDTO {

    public TjvTableCreateDTO (TjvTableType type) {
        super (null, type);
    }
    public TjvTableCreateDTO () { this(null); }

}
