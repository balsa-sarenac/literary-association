package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import upp.team5.literaryassociation.common.dto.*;
import upp.team5.literaryassociation.common.file.service.FileService;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.common.service.NoteService;
import upp.team5.literaryassociation.model.*;
import upp.team5.literaryassociation.publishing.repository.PublishingRequestRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;
import upp.team5.literaryassociation.security.service.RoleService;

import javax.ws.rs.NotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublishingRequestService {
    @Autowired
    private PublishingRequestRepository publishingRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private RoleService roleService;

    private final AuthUserService authUserService;
    private final ModelMapper modelMapper;
    private final NoteService noteService;

    @Autowired
    public PublishingRequestService(AuthUserService authUserService, ModelMapper modelMapper, NoteService noteService) {
        this.authUserService = authUserService;
        this.modelMapper = modelMapper;
        this.noteService = noteService;
    }

    @Transactional
    public void savePublishingRequest(PublishingRequest request) { publishingRequestRepository.save(request); }

    @Transactional
    public List<PublishingRequest> getAll() { return publishingRequestRepository.findAll(); }

    @Transactional
    public Optional<PublishingRequest> getById(Long id) { return publishingRequestRepository.findById(id); }

    @Transactional
    public List<PublishingRequestDTO> getBetaRequests() {
        User user = this.authUserService.getLoggedInUser();

        log.info("Finding user assigned books");
        List<PublishingRequest> publishingRequests = this.publishingRequestRepository.findAllByBetaReaders(user)
                .stream().filter(x -> x.getStatus().equals("Sent to beta readers")).collect(Collectors.toList());
        List<Note> betaReaderNotes = this.noteService.getUserNotes(user, NoteType.COMMENT);
        List<PublishingRequest> commentedRequests = betaReaderNotes.stream().map(Note::getPublishingRequest).collect(Collectors.toList());
        log.info("Removing books user already read and commented");
        publishingRequests.removeIf(commentedRequests::contains);

        List<PublishingRequestDTO> publishingRequestBetaDTOS = new ArrayList<>();
        log.info("Creating publishing book dtos");
        for (PublishingRequest pr : publishingRequests) {
            PublishingRequestDTO prDTO = modelMapper.map(pr, PublishingRequestDTO.class);
            publishingRequestBetaDTOS.add(prDTO);
        }

        return publishingRequestBetaDTOS;
    }

    @Transactional
    public List<PublishingRequest> getRequests(Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(NotFoundException::new);
        List<PublishingRequest> requests = new ArrayList<>(publishingRequestRepository.findByBookAuthors(author));
        log.info(requests.toString());
        return requests;

    }

    @Transactional
    public HashSet<PublishingRequestDTO> getAllEditorRequests(Long editorId) {
        HashSet<PublishingRequestDTO> retVal = new HashSet<>();
        User user = userRepository.findById(editorId).orElseThrow(NotFoundException::new);

        List<PublishingRequest> requests = new ArrayList<>();

        if(user.getRoles().contains(roleService.getByName("ROLE_LECTOR"))) {
            requests = new ArrayList<>( publishingRequestRepository.findByBookLectorsAndStatus(user, "Book is sent to lector"));
        }
        else if(user.getRoles().contains(roleService.getByName("ROLE_CHIEF_EDITOR"))) {
            requests = new ArrayList<>( publishingRequestRepository.findByBookChiefEditor(user));
        }
        else if(user.getRoles().contains(roleService.getByName("ROLE_AUTHOR"))) {
            requests = new ArrayList<>( publishingRequestRepository.findByBookAuthors(user));
        }

        for(PublishingRequest req : requests){
            PublishingRequestDTO pubReq = modelMapper.map(req, PublishingRequestDTO.class);
            retVal.add(pubReq);
        }

        return retVal;
    }

    @Transactional
    public PublishingRequestDTO getPublishingRequestDTO(long requestId) {
        var req = publishingRequestRepository.findById(requestId).orElseThrow(NotFoundException::new);

        PublishingRequestDTO dto = modelMapper.map(req, PublishingRequestDTO.class);

        if(req.getStatus().equals("Book uploaded") ) {
            List<FileDB> sources = null;
            try {
                sources = this.fileService.findAllByPublishingRequest(req);
                sources.removeIf(file -> req.getBook().getId() == file.getUploadedBookId());
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            assert sources != null;
            List<FileDTO> files = sources.stream().map(file -> {
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/publish/documents/")
                        .path(String.valueOf(file.getId()))
                        .toUriString();

                return new FileDTO(
                        file.getName(),
                        fileDownloadUri,
                        file.getType(),
                        file.getData().length);
            }).collect(Collectors.toList());

            dto.setPotentialPlagiarismList(files);
        }

        if(!req.getStatus().equals("New request") && !req.getStatus().equals("Book upload requested") && !req.getStatus().equals("Rejected")) {
            FileDB bookFile = null;

            try {
                bookFile = fileService.getByBookId(req.getBook().getId());
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            assert bookFile != null;
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/publish/documents/")
                    .path(String.valueOf(bookFile.getId()))
                    .toUriString();

            FileDTO bookFileDto = new FileDTO();
            bookFileDto.setName(bookFile.getName());
            bookFileDto.setUrl(fileDownloadUri);
            bookFileDto.setType(bookFile.getType());
            bookFileDto.setSize(bookFile.getData().length);

            dto.getBook().setBookFile(bookFileDto);
        }

        return dto;
    }

    @Transactional
    public List<FileDTO> getFiles(long reqId){
        var req = publishingRequestRepository.findById(reqId).orElseThrow(NotFoundException::new);

        List<FileDB> sources = null;
        try {
            sources = this.fileService.findAllByPublishingRequest(req);
            sources.removeIf(file -> req.getBook().getId() == file.getUploadedBookId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        assert sources != null;
        List<FileDTO> files = sources.stream().map(file -> {
            String fileDownloadUri = "http://localhost:8080/publish/documents/" + file.getId();


            return new FileDTO(
                    file.getName(),
                    fileDownloadUri,
                    file.getType(),
                    file.getData().length);
        }).collect(Collectors.toList());

        return files;
    }

    @Transactional
    public List<FileDTO> getBookFile(long reqId){
        var req = publishingRequestRepository.findById(reqId).orElseThrow(NotFoundException::new);

        List<FileDTO> files = new LinkedList<>();

        FileDB bookFile = null;

        try {
            bookFile = fileService.getByBookId(req.getBook().getId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        assert bookFile != null;
        String fileDownloadUri = "http://localhost:8080/publish/documents/" + bookFile.getId();

        FileDTO bookFileDto = new FileDTO();
        bookFileDto.setName(bookFile.getName());
        bookFileDto.setUrl(fileDownloadUri);
        bookFileDto.setType(bookFile.getType());
        bookFileDto.setSize(bookFile.getData().length);

        files.add(bookFileDto);

        return files;
    }

    @Transactional
    public ResponseEntity<byte[]> getDocument(Long id) {
        FileDB fileDB = this.fileService.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

    @Transactional
    public PublishingRequest getPublishingRequest(long requestId) {
        return publishingRequestRepository.findById(requestId).orElseThrow(NotFoundException::new);
    }


    @Transactional
    public List<UserDTO> getAllBetaReadersForRequest(String requestId) {
        List<UserDTO> ret = new LinkedList<>();
        var request = publishingRequestRepository.findById(Long.parseLong(requestId)).orElseThrow(NotFoundException::new);
        var genres = request.getBook().getGenres();

        if(!genres.isEmpty()){
            var g = genres.iterator().next();

            var users = userRepository.findAllByBetaGenres(g);
            for(User u : users){
                UserDTO userDto = modelMapper.map(u, UserDTO.class);
                ret.add(userDto);
            }
        }


        return ret;
    }

    @Transactional
    public PublishingRequestDTO getBetaRequest(Long id) {
        PublishingRequest publishingRequest = getPublishingRequest(id);
        PublishingRequestDTO publishingRequestDTO = modelMapper.map(publishingRequest, PublishingRequestDTO.class);
        FileDB bookFile = fileService.getByBookId(publishingRequest.getBook().getId());
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/membership-requests/documents/")
                .path(String.valueOf(bookFile.getId()))
                .toUriString();

        FileDTO fileDTO = new FileDTO(bookFile.getName(),
                fileDownloadUri,
                bookFile.getType(),
                bookFile.getData().length);

        publishingRequestDTO.getBook().setBookFile(fileDTO);
        return publishingRequestDTO;
    }

    @Transactional
    PublishingRequest getPublishingRequest(Long id) {
        return this.publishingRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request with given id doesn't exist"));
    }

}
