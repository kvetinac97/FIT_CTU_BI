package cz.kvetinac97.dto;

import java.util.List;

public class BookCreateDTO {

    private final String name;
    private final List<Integer> authors;
    private final Integer translationOfBook;

    public BookCreateDTO(String name, List<Integer> authors, Integer translationOfBook) {
        this.name = name;
        this.authors = authors;
        this.translationOfBook = translationOfBook;
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
