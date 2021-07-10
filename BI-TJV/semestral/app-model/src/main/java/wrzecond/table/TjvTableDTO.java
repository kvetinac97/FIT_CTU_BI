package wrzecond.table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class TjvTableDTO {

    // === PROPERTIES ===
    protected final Integer id;
    @Getter private final TjvTableType type;

}
