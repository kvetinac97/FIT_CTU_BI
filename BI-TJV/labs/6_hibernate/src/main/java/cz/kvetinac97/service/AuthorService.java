package cz.kvetinac97.service;

import cz.kvetinac97.dto.AuthorCreateDTO;
import cz.kvetinac97.dto.AuthorDTO;
import cz.kvetinac97.entity.Author;
import cz.kvetinac97.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private Optional<Author> getByID ( int id ) {
        return Optional.of(authorRepository.getOne(id));
    }

    public List<Author> findByIDs ( List<Integer> ids ) {
        return ids.stream().map(this::getByID)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Optional<AuthorDTO> getByIDAsDTO ( int id ) {
        return toDTO(getByID(id));
    }

    public List<AuthorDTO> findAll () {
        return authorRepository.findAll()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO create ( AuthorCreateDTO authorCreateDTO ) {
        return toDTO ( authorRepository.save (
                new Author(authorCreateDTO.getFirstName(), authorCreateDTO.getLastName())
        ) );
    }

    @Transactional
    public AuthorDTO update ( int id, AuthorCreateDTO authorCreateDTO ) {
        Optional<Author> optional = getByID(id);
        if ( !optional.isPresent() )
            return create(authorCreateDTO); // throw exception

        Author author = optional.get();
        author.setFirstName(authorCreateDTO.getFirstName());
        author.setLastName(authorCreateDTO.getLastName());
        return toDTO(author);
    }

    private AuthorDTO toDTO ( Author author ) {
        return new AuthorDTO(author.getId(), author.getFirstName(), author.getLastName());
    }

    private Optional<AuthorDTO> toDTO ( Optional<Author> optional ) {
        return optional.map(this::toDTO);
    }

}
