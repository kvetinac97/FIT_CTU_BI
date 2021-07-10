package cz.kvetinac97.service;

import cz.kvetinac97.dto.BookCreateDTO;
import cz.kvetinac97.dto.BookDTO;
import cz.kvetinac97.entity.Author;
import cz.kvetinac97.entity.Book;
import cz.kvetinac97.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    public Optional<BookDTO> findByIdAsDTO(int id) {
        return toDTO(findById(id));
    }

    @Transactional
    public BookDTO create(BookCreateDTO bookDTO) throws Exception {
        List<Author> authors = authorService.findByIDs(bookDTO.getAuthors());
        if (authors.size() != bookDTO.getAuthors().size())
            throw new Exception("some authors not found");

        Book translationOfBook = bookDTO.getTranslationOfBook() == null ? null :
                findById(bookDTO.getTranslationOfBook()).orElseThrow(() -> new Exception("no such translation book"));

        Book book = new Book(bookDTO.getName(), authors, translationOfBook);

        return toDTO(bookRepository.save(book));
    }

    @Transactional
    public BookDTO update(int id, BookCreateDTO bookDTO) throws Exception {
        Optional<Book> optionalBook = findById(id);
        if (!optionalBook.isPresent())
            throw new Exception("no such book");
        Book book = optionalBook.get();
        book.setName(bookDTO.getName());

        List<Author> authors = authorService.findByIDs(bookDTO.getAuthors());
        if (authors.size() != bookDTO.getAuthors().size())
            throw new Exception("some authors not found");
        book.setAuthors(authors);

        book.setTranslationOfBook(bookDTO.getTranslationOfBook() == null ?
                null :
                findById(bookDTO.getTranslationOfBook()).orElseThrow(() -> new Exception(
                        "no such translation book")));

        return toDTO(book);
    }

    private BookDTO toDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getName(),
                book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()),
                book.getTranslationOfBook() != null ? book.getTranslationOfBook().getId() : null
        );
    }

    private Optional<BookDTO> toDTO(Optional<Book> book) {
        return book.map(this::toDTO);
    }

}