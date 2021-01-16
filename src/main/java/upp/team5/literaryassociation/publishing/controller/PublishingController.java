package upp.team5.literaryassociation.publishing.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.common.dto.ChiefEditorResponse;
import upp.team5.literaryassociation.common.dto.ProcessDTO;
import upp.team5.literaryassociation.common.dto.PublishingRequestDTO;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.HashSet;
import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/publish")
public class PublishingController {
    @Autowired
    private RuntimeService runtimeService;

    private final PublishingRequestService publishingRequestService;

    @Autowired
    PublishingController(PublishingRequestService publishingRequestService){
        this.publishingRequestService = publishingRequestService;
    }

    @PreAuthorize("hasAuthority('ROLE_AUTHOR')")
    @GetMapping(name = "bookPublishing", path="/start-book-publishing")
    public ResponseEntity<ProcessDTO> startBookPublishingProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("book-publishing");

        ProcessDTO processDTO = new ProcessDTO(processInstance.getId());

        return new ResponseEntity<>(processDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_AUTHOR')")
    @GetMapping(name = "getAuthorRequests", path="/author-requests/{authorId}")
    public ResponseEntity<HashSet<PublishingRequestDTO>> getAuthorRequests(@PathVariable String authorId) throws JsonProcessingException {
        ModelMapper modelMapper = new ModelMapper();
        HashSet<PublishingRequestDTO> retVal = new HashSet<>();

        List<PublishingRequest> requests = publishingRequestService.getRequests(Long.parseLong(authorId));
        for(PublishingRequest req : requests){
            PublishingRequestDTO pubReq = modelMapper.map(req, PublishingRequestDTO.class);
            retVal.add(pubReq);
        }
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    @GetMapping(name = "getChiefEditorRequests", path="/chiefEditor-requests/{editorId}")
    public ResponseEntity<HashSet<PublishingRequestDTO>> getChiefEditorRequests(@PathVariable String editorId) throws JsonProcessingException {
        HashSet<PublishingRequestDTO> retVal = publishingRequestService.getEditorRequests(Long.parseLong(editorId));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    @GetMapping(name = "getRequest", path="/get-request/{requestId}")
    public ResponseEntity<PublishingRequestDTO> getRequest(@PathVariable String requestId) throws JsonProcessingException {
        ModelMapper modelMapper = new ModelMapper();
        PublishingRequest publishingRequest = publishingRequestService.getPublishingRequest(Long.parseLong(requestId));
        PublishingRequestDTO dto = modelMapper.map(publishingRequest, PublishingRequestDTO.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    @PostMapping(name = "read", path="/read")
    public void readBook(@RequestBody ChiefEditorResponse response){
        publishingRequestService.readBook(response);
    }

    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    @GetMapping(name = "getRequestsPlagiarismCheck", path="/get-requests-plagiarism-check/{editorId}")
    public ResponseEntity<HashSet<PublishingRequestDTO>> GetRequestsPlagiarismCheck(@PathVariable String editorId) throws JsonProcessingException {
        HashSet<PublishingRequestDTO> retRequests = publishingRequestService.getEditorRequestsPlagiarismCheck(Long.parseLong(editorId));
        return new ResponseEntity<>(retRequests, HttpStatus.OK);
    }

}
