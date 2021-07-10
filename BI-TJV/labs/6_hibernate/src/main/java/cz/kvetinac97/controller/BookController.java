package cz.kvetinac97.controller;

import cz.kvetinac97.dto.BookCreateDTO;
import cz.kvetinac97.dto.BookDTO;
import cz.kvetinac97.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/book/all")
    List<BookDTO> all() {
        return bookService.findAll();
    }

    @GetMapping("/book/{id}")
    BookDTO byId(@PathVariable int id) {
        return bookService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/book")
    BookDTO save(@RequestBody BookCreateDTO book) throws Exception {
        return bookService.create(book);
    }

    @PutMapping("/book/{id}")
    BookDTO save(@PathVariable int id, @RequestBody BookCreateDTO book) throws Exception {
        return bookService.update(id, book);
    }
}