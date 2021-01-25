package upp.team5.literaryassociation.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;

import java.util.HashSet;
import java.util.List;

@Repository
public interface PublishingRequestRepository extends JpaRepository<PublishingRequest, Long> {


    HashSet<PublishingRequest> findByBookAuthors(User book_authors);

    List<PublishingRequest> findAllByBetaReaders(User beatReader);

    HashSet<PublishingRequest> findByBookChiefEditor(User chiefEditor);

    HashSet<PublishingRequest> findByBookLectors(User lector);
}
