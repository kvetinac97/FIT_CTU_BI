package wrzecond.order_food;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import wrzecond.ICreateDTO;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
public class TjvOrderFoodCreateDTO extends TjvOrderFoodDTO implements ICreateDTO {

    // === PROPERTIES
    @Getter private final Integer food;

    public TjvOrderFoodCreateDTO (Integer order, Integer food, Timestamp datetime, Integer count) {
        super (null, order, datetime, count);
        this.food = food;
    }
    public TjvOrderFoodCreateDTO () {
        this (null, null, null, null);
    }

}
