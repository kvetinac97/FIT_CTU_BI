package wrzecond.employee;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class TjvEmployeeDTO {

    // === PROPERTIES ===
    protected final Integer id;
    protected final String password;

    @Getter private final String username;
    @Getter private final String firstName;
    @Getter private final String lastName;
    @Getter protected final Boolean admin;

}
