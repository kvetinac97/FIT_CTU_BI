package wrzecond.employee;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TjvPasswordDTO {

    @Getter private final String oldPassword;
    @Getter private final String newPassword;

}
