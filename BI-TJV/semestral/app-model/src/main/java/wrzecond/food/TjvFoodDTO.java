package wrzecond.food;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class TjvFoodDTO {

    // === PROPERTIES ===
    protected final Integer id;
    @Getter private final String name;
    @Getter private final Integer price;
    @Getter private final Boolean cooked;
    @Getter private final List<Integer> allergens;

}
