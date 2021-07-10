package wrzecond.order_food;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import wrzecond.IReadDTO;
import wrzecond.food.TjvFoodReadDTO;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
public class TjvOrderFoodReadDTO extends TjvOrderFoodDTO implements IReadDTO {

    // === PROPERTIES
    @Getter private final TjvFoodReadDTO food;

    public TjvOrderFoodReadDTO (Integer id, Integer order, TjvFoodReadDTO food,
                                   Timestamp datetime, Integer count) {
        super (id, order, datetime, count);
        this.food = food;
    }

    @Override
    public int getId () {
        return id;
    }

    @Override
    public String getDisplayName() {
        return getOrder() + " - " + food.getDisplayName();
    }

}
