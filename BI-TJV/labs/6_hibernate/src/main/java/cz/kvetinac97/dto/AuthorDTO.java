package cz.kvetinac97.dto;

// Použití pro HTTP request
public class AuthorDTO {

    private final int id;
    private final String firstName;
    private final String lastName;

    public AuthorDTO(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
