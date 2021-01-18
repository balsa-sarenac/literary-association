package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.dto.ChiefEditorResponse;
import upp.team5.literaryassociation.common.dto.PublishingRequestDTO;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
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
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IdentityService identityService;

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
        List<PublishingRequestDTO> publishingRequestBetaDTOS = new ArrayList<>();

        log.info("Creating publishing book dtos");
        ModelMapper modelMapper = new ModelMapper();
        for (PublishingRequest pr : publishingRequests) {
            PublishingRequestDTO prDTO = modelMapper.map(pr, PublishingRequestDTO.class);
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
        List<PublishingRequest> requests = publishingRequestRepository.findByBookChiefEditorAndReviewed(chiefEditor, false).stream().collect(Collectors.toList());

        ModelMapper modelMapper = new ModelMapper();
        HashSet<PublishingRequestDTO> retVal = new HashSet<>();

        for(PublishingRequest req : requests){
            PublishingRequestDTO pubReq = modelMapper.map(req, PublishingRequestDTO.class);
            retVal.add(pubReq);
        }

        return retVal;

    }

    public HashSet<PublishingRequestDTO> getEditorRequestsPlagiarismCheck(Long editorId) {
        User chiefEditor = userRepository.findById(editorId).orElseThrow(NotFoundException::new);
        List<PublishingRequest> requests = new ArrayList<>(publishingRequestRepository.findByBookChiefEditorAndSynopsisAcceptedAndOriginalChecked(chiefEditor, true, false));

        ModelMapper modelMapper = new ModelMapper();
        HashSet<PublishingRequestDTO> retRequests = new HashSet<>();

        for(PublishingRequest req : requests){
            /*if(req.getBook() != null) {
                PublishingRequestDTO request = modelMapper.map(req, PublishingRequestDTO.class);
                retRequests.add(request);
            }*/
            PublishingRequestDTO request = modelMapper.map(req, PublishingRequestDTO.class);
            retRequests.add(request);
        }
        return retRequests;
    }

    public PublishingRequest getPublishingRequest(long requestId) {
        return publishingRequestRepository.findById(requestId).orElseThrow(NotFoundException::new);
    }

    public void reviewRequest(ChiefEditorResponse response) {
        PublishingRequest publishingRequest = publishingRequestRepository.findById(response.getPublishingRequestId()).orElseThrow(NotFoundException::new);
        publishingRequest.setReviewed(true);

        if(response.getResponse()){
            publishingRequest.setStatus("Book upload requested");
        }
        else{
            publishingRequest.setStatus("Reading rejected");
        }

        publishingRequest.setSynopsisAccepted(response.getResponse());
        publishingRequestRepository.save(publishingRequest);

        ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("book-publishing")
                .variableValueEquals("publishing-request-id", response.getPublishingRequestId())
                .singleResult();

        User loggedUser = authUserService.getLoggedInUser();
        org.camunda.bpm.engine.identity.User camundaUser = identityService.createUserQuery().userId(String.valueOf(loggedUser.getId())).singleResult();
        Task task = this.taskService.createTaskQuery().processInstanceId(pi.getId()).active().singleResult();
        taskService.claim(task.getId(), camundaUser.getId());

        var u = task.getAssignee();

        if(u.equals(camundaUser.getId())){
            runtimeService.setVariable(pi.getProcessInstanceId(), "readApproved", response.getResponse());

            log.info("Completing task");
            taskService.complete(task.getId());
        }
    }

    public void originalBook(ChiefEditorResponse response) {
        PublishingRequest publishingRequest = publishingRequestRepository.findById(response.getPublishingRequestId()).orElseThrow(NotFoundException::new);
        publishingRequest.setOriginalChecked(true);
        publishingRequest.setOriginal(response.getResponse());
        publishingRequestRepository.save(publishingRequest);

        ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("book-publishing")
                .variableValueEquals("publishing-request-id", response.getPublishingRequestId())
                .singleResult();

        User loggedUser = authUserService.getLoggedInUser();
        org.camunda.bpm.engine.identity.User camundaUser = identityService.createUserQuery().userId(String.valueOf(loggedUser.getId())).singleResult();
        Task task = this.taskService.createTaskQuery().processInstanceId(pi.getId()).active().singleResult();

        var u = task.getAssignee();

        if(u.equals(camundaUser.getId())){
            runtimeService.setVariable(pi.getProcessInstanceId(), "original", response.getResponse());

            log.info("Completing task");
            taskService.complete(task.getId());
        }
    }
}
