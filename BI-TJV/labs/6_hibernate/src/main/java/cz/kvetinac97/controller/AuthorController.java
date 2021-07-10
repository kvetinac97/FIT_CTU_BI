package cz.kvetinac97.controller;

import cz.kvetinac97.dto.AuthorCreateDTO;
import cz.kvetinac97.dto.AuthorDTO;
import cz.kvetinac97.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/author/all")
    List<AuthorDTO> all() {
        return authorService.findAll();
    }

    @GetMapping("/author/{id}")
    AuthorDTO byId(@PathVariable int id) {
        return authorService.getByIDAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/author")
    AuthorDTO save(@RequestBody AuthorCreateDTO author) {
        return authorService.create(author);
    }

    @PutMapping("/author/{id}")
    AuthorDTO save(@PathVariable int id, @RequestBody AuthorCreateDTO author) {
        return authorService.update(id, author);
    }

}