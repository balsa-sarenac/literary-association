package upp.team5.literaryassociation.publishing.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.publishing.repository.PublishingRequestRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PublishingRequestService {
    @Autowired
    private PublishingRequestRepository publishingRequestRepository;

    public void savePublishingRequest(PublishingRequest request) { publishingRequestRepository.save(request); }

    public List<PublishingRequest> getAll() { return publishingRequestRepository.findAll(); }

    public Optional<PublishingRequest> getById(Long id) { return publishingRequestRepository.findById(id); }
}
