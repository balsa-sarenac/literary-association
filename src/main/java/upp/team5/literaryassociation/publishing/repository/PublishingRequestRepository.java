package upp.team5.literaryassociation.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;

import java.util.HashSet;

@Repository
public interface PublishingRequestRepository extends JpaRepository<PublishingRequest, Long> {
    HashSet<PublishingRequest> findPublishingRequestByBookAuthors(User bookAuthor);
}
