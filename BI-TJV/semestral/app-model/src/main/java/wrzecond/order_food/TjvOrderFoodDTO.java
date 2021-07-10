package wrzecond.order_food;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.Timestamp;

@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class TjvOrderFoodDTO {

    // === PROPERTIES ===
    protected final Integer id;
    @Getter private final Integer order;

    @Getter private final Timestamp datetime;
    @Getter private final Integer count;

}
