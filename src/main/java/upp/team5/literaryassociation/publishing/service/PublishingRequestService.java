package upp.team5.literaryassociation.publishing.service;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.dto.PublishingRequestDTO;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.repository.PublishingRequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PublishingRequestService {
    @Autowired
    private PublishingRequestRepository publishingRequestRepository;

    private final AuthUserService authUserService;

    @Autowired
    public PublishingRequestService(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    public void savePublishingRequest(PublishingRequest request) { publishingRequestRepository.save(request); }

    public List<PublishingRequest> getAll() { return publishingRequestRepository.findAll(); }

    public Optional<PublishingRequest> getById(Long id) { return publishingRequestRepository.findById(id); }

    public List<PublishingRequestDTO> getBetaRequests() {
        User user = this.authUserService.getLoggedInUser();

        log.info("Finding user assigned books");
        List<PublishingRequest> publishingRequests = this.publishingRequestRepository.findAllByBetaReaders(user);
        List<PublishingRequestDTO> publishingRequestDTOS = new ArrayList<>();

        log.info("Creating publishing book dtos");
        ModelMapper modelMapper = new ModelMapper();
        for (PublishingRequest pr : publishingRequests) {
            PublishingRequestDTO prDTO = modelMapper.map(pr, PublishingRequestDTO.class);
            publishingRequestDTOS.add(prDTO);
        }

        return publishingRequestDTOS;
    }
}
