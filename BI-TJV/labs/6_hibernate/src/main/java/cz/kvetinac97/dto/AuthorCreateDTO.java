package cz.kvetinac97.dto;

// Použití pro HTTP request
public class AuthorCreateDTO {

    private final String firstName;
    private final String lastName;

    public AuthorCreateDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
