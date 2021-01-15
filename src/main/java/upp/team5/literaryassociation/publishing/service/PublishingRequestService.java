package upp.team5.literaryassociation.publishing.service;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.dto.PublishingRequestDTO;
import upp.team5.literaryassociation.publishing.repository.PublishingRequestRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublishingRequestService {
    @Autowired
    private PublishingRequestRepository publishingRequestRepository;
    @Autowired
    private UserRepository userRepository;

    public void savePublishingRequest(PublishingRequest request) { publishingRequestRepository.save(request); }

    public List<PublishingRequest> getAll() { return publishingRequestRepository.findAll(); }

    public Optional<PublishingRequest> getById(Long id) { return publishingRequestRepository.findById(id); }

    public List<PublishingRequest> getRequests(Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(NotFoundException::new);
        List<PublishingRequest> requests = publishingRequestRepository.findByBookAuthors(author).stream().collect(Collectors.toList());
        log.info(requests.toString());
        return requests;

    }

    public HashSet<PublishingRequestDTO> getEditorRequests(Long editorId) {
        User chiefEditor = userRepository.findById(editorId).orElseThrow(NotFoundException::new);
        List<PublishingRequest> requests = publishingRequestRepository.findByBookChiefEditorAndApproved(chiefEditor, false).stream().collect(Collectors.toList());

        ModelMapper modelMapper = new ModelMapper();
        HashSet<PublishingRequestDTO> retVal = new HashSet<>();

        for(PublishingRequest req : requests){
            PublishingRequestDTO pubReq = modelMapper.map(req, PublishingRequestDTO.class);
            retVal.add(pubReq);
        }

        return retVal;
    }
}
