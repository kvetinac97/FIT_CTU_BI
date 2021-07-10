package wrzecond.allergen;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
abstract class TjvAllergenDTO {

    // === PROPERTIES ===
    protected final Integer id;
    @Getter private final String name;

}
