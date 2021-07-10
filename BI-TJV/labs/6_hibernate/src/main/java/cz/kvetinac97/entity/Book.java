package cz.kvetinac97.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String name;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToOne
    @JoinColumn(name = "translation_id")
    private Book translationOfBook;

    public Book(String name, List<Author> authors, Book translationOfBook) {
        this.name = name;
        this.authors = authors;
        this.translationOfBook = translationOfBook;
    }

    public Book() {}

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<Author> getAuthors() {
        return authors;
    }
    public Book getTranslationOfBook() {
        return translationOfBook;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setTranslationOfBook(Book translationOfBook) {
        this.translationOfBook = translationOfBook;
    }

}
