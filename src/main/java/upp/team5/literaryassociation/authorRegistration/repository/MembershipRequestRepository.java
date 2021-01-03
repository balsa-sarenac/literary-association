package upp.team5.literaryassociation.authorRegistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upp.team5.literaryassociation.model.MembershipRequest;

@Repository
public interface MembershipRequestRepository extends JpaRepository<MembershipRequest, Long> {
}
