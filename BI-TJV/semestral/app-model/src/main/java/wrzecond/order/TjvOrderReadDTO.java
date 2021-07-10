package wrzecond.order;

import wrzecond.IReadDTO;

import java.sql.Timestamp;

public class TjvOrderReadDTO extends TjvOrderDTO implements IReadDTO {

    public TjvOrderReadDTO (Integer id, Integer table, Timestamp datetime, Boolean completed) {
        super (id, table, datetime, completed);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return Integer.toString(getId());
    }

}
