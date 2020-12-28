package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.repository.PublishingRequestRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;
import java.util.HashSet;

@Service
@Slf4j
public class PublishingService {
    private PublishingRequestRepository publishingRequestRepository;
    private UserRepository userRepository;

    PublishingService(PublishingRequestRepository publishingRequestRepository, UserRepository userRepository){
        this.publishingRequestRepository = publishingRequestRepository;
        this.userRepository = userRepository;
    }

    public HashSet<PublishingRequest> getRequests(Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(NotFoundException::new);
        return publishingRequestRepository.findPublishingRequestByBookAuthors(author);
    }
}
