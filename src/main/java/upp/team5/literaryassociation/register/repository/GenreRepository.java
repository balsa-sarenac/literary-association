package upp.team5.literaryassociation.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    public Genre findByName(String name);
}
