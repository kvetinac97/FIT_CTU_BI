package cz.kvetinac97.repository;

import cz.kvetinac97.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Navíc ještě CRUD | Sorting & Paging repository
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
