package upp.team5.literaryassociation.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.User;

import java.util.HashSet;

public interface BookRepository extends JpaRepository<Book, Long> {
    HashSet<Book> findByAuthors(User authors);
}
