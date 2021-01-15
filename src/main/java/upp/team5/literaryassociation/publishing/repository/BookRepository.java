package upp.team5.literaryassociation.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
