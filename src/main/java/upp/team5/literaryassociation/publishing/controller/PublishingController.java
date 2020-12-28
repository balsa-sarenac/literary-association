package upp.team5.literaryassociation.publishing.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.common.dto.ProcessDTO;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.publishing.service.PublishingService;

import java.util.HashSet;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/publish")
public class PublishingController {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;

    private PublishingService publishingService;

    PublishingController(PublishingService publishingService){
        this.publishingService = publishingService;
    }

    @GetMapping(name = "bookPublishing", path="/start-book-publishing")
    public ResponseEntity<ProcessDTO> startBookPublishingProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1iaa6uo");
        ProcessDTO processDTO = new ProcessDTO(processInstance.getId());

        return new ResponseEntity<>(processDTO, HttpStatus.OK);
    }

    @GetMapping(name = "getAuthorRequests", path="/get/author-requests/{authorId}")
    public ResponseEntity<?> getAuthorRequests(@PathVariable String authorId) throws JsonProcessingException {
        HashSet<PublishingRequest> requests = publishingService.getRequests(Long.parseLong(authorId));
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(requests);
        log.info(json);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
