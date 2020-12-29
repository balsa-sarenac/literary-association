package upp.team5.literaryassociation.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.PublishingRequest;

public interface PublishingRequestRepository extends JpaRepository<PublishingRequest, Long> {

}
