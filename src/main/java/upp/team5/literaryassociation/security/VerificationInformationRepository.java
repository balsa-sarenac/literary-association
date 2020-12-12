package upp.team5.literaryassociation.security;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.VerificationInformation;


public interface VerificationInformationRepository extends JpaRepository<VerificationInformation, Long> {
    VerificationInformation getVerificationInformationByHash(String hash);
}
