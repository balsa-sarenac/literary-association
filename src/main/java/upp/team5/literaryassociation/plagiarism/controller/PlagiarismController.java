package upp.team5.literaryassociation.plagiarism.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.common.dto.PlagiarismComplaintDTO;
import upp.team5.literaryassociation.common.dto.ProcessDTO;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping( path = "/plagiarism")
public class PlagiarismController {
    @Autowired
    private RuntimeService runtimeService;

    PlagiarismController(){}

    @PreAuthorize("hasAuthority('ROLE_AUTHOR')")
    @GetMapping(name = "plagiarismStart", path="/start-plagiarism")
    public ResponseEntity<ProcessDTO> startBookPublishingProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("plagiarism-process");

        ProcessDTO processDTO = new ProcessDTO(processInstance.getId());

        return new ResponseEntity<>(processDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_AUTHOR')")
    @PostMapping(name = "fileComplaint", path="/file-a-complaint/{authorId}")
    public ResponseEntity<> fileComplaint(@RequestBody PlagiarismComplaintDTO plagiarismComplaintDTO) {

    }
}
