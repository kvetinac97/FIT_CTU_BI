package wrzecond.employee;

import wrzecond.ICreateDTO;

public class TjvEmployeeCreateDTO extends TjvEmployeeDTO implements ICreateDTO {

    // === CONSTRUCTORS ===
    public TjvEmployeeCreateDTO () {
        this (null, null, null, null, null);
    }
    public TjvEmployeeCreateDTO (String username, String password,
                                 String firstName, String lastName, Boolean admin) {
        super(null, password, username, firstName, lastName, admin);
    }

    // === GETTERS ===
    public String getPassword () {
        return password;
    }
    public Boolean isAdmin () {
        return admin;
    }

}
