package upp.team5.literaryassociation.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}
