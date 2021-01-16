package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.dto.NoteDTO;
import upp.team5.literaryassociation.common.dto.PublishingRequestBetaDTO;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.common.service.NoteService;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.model.NoteType;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.dto.PublishingRequestDTO;
import upp.team5.literaryassociation.publishing.repository.PublishingRequestRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
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

    private final AuthUserService authUserService;
    private final ModelMapper modelMapper;
    private final NoteService noteService;

    @Autowired
    public PublishingRequestService(AuthUserService authUserService, ModelMapper modelMapper, NoteService noteService) {
        this.authUserService = authUserService;
        this.modelMapper = modelMapper;
        this.noteService = noteService;
    }

    public void savePublishingRequest(PublishingRequest request) { publishingRequestRepository.save(request); }

    public List<PublishingRequest> getAll() { return publishingRequestRepository.findAll(); }

    public Optional<PublishingRequest> getById(Long id) { return publishingRequestRepository.findById(id); }

    public List<PublishingRequestBetaDTO> getBetaRequests() {
        User user = this.authUserService.getLoggedInUser();

        log.info("Finding user assigned books");
        List<PublishingRequest> publishingRequests = this.publishingRequestRepository.findAllByBetaReaders(user);
        List<Note> betaReaderNotes = this.noteService.getUserNotes(user, NoteType.COMMENT);
        List<PublishingRequest> commentedRequests = betaReaderNotes.stream().map(Note::getPublishingRequest).collect(Collectors.toList());
        log.info("Removing books user already read and commented");
        publishingRequests.removeIf(commentedRequests::contains);

        List<PublishingRequestBetaDTO> publishingRequestBetaDTOS = new ArrayList<>();
        log.info("Creating publishing book dtos");
        for (PublishingRequest pr : publishingRequests) {
            PublishingRequestBetaDTO prDTO = modelMapper.map(pr, PublishingRequestBetaDTO.class);
            publishingRequestBetaDTOS.add(prDTO);
        }

        return publishingRequestBetaDTOS;
    }

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

    public PublishingRequestBetaDTO getBetaRequest(Long id) {
        PublishingRequest publishingRequest = getPublishingRequest(id);
        return modelMapper.map(publishingRequest, PublishingRequestBetaDTO.class);
    }

    private PublishingRequest getPublishingRequest(Long id) {
        return this.publishingRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request with given id doesn't exist"));
    }

    public void saveNotes(Long id, NoteDTO notes) {
        User betaReader = this.authUserService.getLoggedInUser();
        PublishingRequest publishingRequest = getPublishingRequest(id);
        Note note = modelMapper.map(notes, Note.class);
        note.setPublishingRequest(publishingRequest);
        note.setUser(betaReader);

        this.noteService.saveNote(note);
    }
}
