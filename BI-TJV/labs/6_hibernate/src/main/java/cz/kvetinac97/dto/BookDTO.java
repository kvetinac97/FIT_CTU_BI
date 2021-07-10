package cz.kvetinac97.dto;

import java.util.List;

// Použití pro HTTP request
public class BookDTO {

    private final int id;
    private final String name;
    private final List<Integer> authors;
    private final Integer translationOfBook;

    public BookDTO(int id, String name, List<Integer> authors, Integer translationOfBook) {
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.translationOfBook = translationOfBook;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getAuthors() {
        return authors;
    }

    public Integer getTranslationOfBook() {
        return translationOfBook;
    }

}
