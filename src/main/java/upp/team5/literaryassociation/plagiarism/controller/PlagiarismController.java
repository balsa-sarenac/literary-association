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
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping( path = "/plagiarism")
public class PlagiarismController {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private AuthUserService authUserService;

    private final PlagiarismComplaintService plagiarismComplaintService;

    PlagiarismController(PlagiarismComplaintService plagiarismComplaintService){
        this.plagiarismComplaintService = plagiarismComplaintService;
    }

    @PreAuthorize("hasAuthority('ROLE_AUTHOR')")
    @GetMapping(name = "plagiarismStart", path="/start-plagiarism")
    public ResponseEntity<ProcessDTO> startBookPublishingProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("plagiarism-process");

        runtimeService.setVariable(processInstance.getId(), "assignee", authUserService.getLoggedInUser().getId());

        ProcessDTO processDTO = new ProcessDTO(processInstance.getId());

        return new ResponseEntity<>(processDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_AUTHOR')")
    @PostMapping(name = "fileComplaint", path="/file-a-complaint/{authorId}/{processId}")
    public void fileComplaint(@RequestBody PlagiarismComplaintDTO plagiarismComplaintDTO, @PathVariable String authorId, @PathVariable String processId) {
        plagiarismComplaintService.processComplaint(plagiarismComplaintDTO, Long.parseLong(authorId), processId);
    }
}
