package upp.team5.literaryassociation.publishing.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upp.team5.literaryassociation.common.dto.ProcessDTO;

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

    @GetMapping(name = "bookPublishing", path="/start-book-publishing")
    public ResponseEntity<ProcessDTO> startBookPublishingProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1iaa6uo");
        ProcessDTO processDTO = new ProcessDTO(processInstance.getId());

        return new ResponseEntity<>(processDTO, HttpStatus.OK);
    }
}
