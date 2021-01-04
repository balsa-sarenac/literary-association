package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.repository.PublishingRequestRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublishingService {
    private final PublishingRequestRepository publishingRequestRepository;
    private final UserRepository userRepository;

    PublishingService(PublishingRequestRepository publishingRequestRepository, UserRepository userRepository){
        this.publishingRequestRepository = publishingRequestRepository;
        this.userRepository = userRepository;
    }

    public List<PublishingRequest> getRequests(Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(NotFoundException::new);
        List<PublishingRequest> requests = publishingRequestRepository.findByBookAuthors(author).stream().collect(Collectors.toList());
        log.info(requests.toString());
        return requests;

    }
}
