package wrzecond.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.Timestamp;

@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class TjvOrderDTO {

    // === PROPERTIES ===
    protected final Integer id;
    @Getter private final Integer table;
    @Getter private final Timestamp datetime;
    @Getter private final Boolean completed;

}
