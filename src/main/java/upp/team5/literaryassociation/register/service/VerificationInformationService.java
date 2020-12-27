package upp.team5.literaryassociation.register.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.VerificationInformation;
import upp.team5.literaryassociation.register.repository.VerificationInformationRepository;

@Service
@Slf4j
public class VerificationInformationService {

    private VerificationInformationRepository verificationInformationRepository;

    public VerificationInformationService(VerificationInformationRepository verificationInformationRepository) {
        this.verificationInformationRepository = verificationInformationRepository;
    }

    public void AddNew(VerificationInformation verificationInformation) {
        verificationInformationRepository.save(verificationInformation);
    }
}
