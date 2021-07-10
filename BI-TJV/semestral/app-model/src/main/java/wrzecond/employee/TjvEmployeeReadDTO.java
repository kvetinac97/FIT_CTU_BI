package wrzecond.employee;

import wrzecond.IReadDTO;

public class TjvEmployeeReadDTO extends TjvEmployeeDTO implements IReadDTO {

    public TjvEmployeeReadDTO (Integer id, String username, String firstName, String lastName, Boolean admin) {
        super (id, null, username, firstName, lastName, admin);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getDisplayName () {
        return getFirstName() + " " + getLastName();
    }

}
