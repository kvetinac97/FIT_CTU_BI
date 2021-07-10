package wrzecond.order;

import wrzecond.ICreateDTO;

import java.sql.Timestamp;

public class TjvOrderCreateDTO extends TjvOrderDTO implements ICreateDTO {

    public TjvOrderCreateDTO (Integer table, Timestamp datetime) {
        this (table, datetime, false);
    }
    public TjvOrderCreateDTO (Integer table, Timestamp datetime, Boolean completed) {
        super (null, table, datetime, completed != null && completed);
    }
    public TjvOrderCreateDTO () {
        this (null, null, null);
    }

}
