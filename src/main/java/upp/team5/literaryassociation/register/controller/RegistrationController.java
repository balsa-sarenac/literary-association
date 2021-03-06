package upp.team5.literaryassociation.register.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.common.dto.ProcessDTO;
import upp.team5.literaryassociation.exception.UserAlreadyExistsException;
import upp.team5.literaryassociation.register.repository.VerificationInformationRepository;
import upp.team5.literaryassociation.common.dto.FormSubmissionFieldDTO;
import upp.team5.literaryassociation.common.dto.RegistrationDTO;
import upp.team5.literaryassociation.register.service.RegistrationService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/auth")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final VerificationInformationRepository verificationInformationRepository;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IdentityService identityService;

    @Autowired
    public RegistrationController(RegistrationService registrationService, VerificationInformationRepository verificationInformationRepository) {
        this.registrationService = registrationService;
        this.verificationInformationRepository = verificationInformationRepository;
    }

    @PostMapping(name = "register", path = "/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDTO registrationDTO) throws UserAlreadyExistsException {
        return registrationService.register(registrationDTO);
    }


    @GetMapping(name = "authorReg", path="/start-author-reg")
    public ResponseEntity<ProcessDTO> startAuthorRegProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("author-reg");

        ProcessDTO processDTO = new ProcessDTO(processInstance.getProcessInstanceId());

        return new ResponseEntity<>(processDTO, HttpStatus.OK);
    }

    @GetMapping(name = "readerReg", path="/start-reader-reg")
    public ResponseEntity<ProcessDTO> startReaderRegProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("reader-registration-process");

        ProcessDTO processDTO = new ProcessDTO(processInstance.getId());

        return new ResponseEntity<>(processDTO, HttpStatus.OK);
    }

  

    @GetMapping(name = "clickVerification", path = "/clickVerification/{email}/{hash}")
    public void clickVerification(@PathVariable String email, @PathVariable String hash, HttpServletResponse httpServletResponse) throws IOException {
        log.info("Verification link clicked");

        var verInf = verificationInformationRepository.getVerificationInformationByHash(hash);
        if(verInf != null) {
            if(verInf.getEmail().equals(email)){
                MessageCorrelationResult result = runtimeService.createMessageCorrelation("LinkClicked")
                        .processInstanceBusinessKey(verInf.getProcessBusinessKey())
                        .setVariable("userVerified", true)
                        .correlateWithResult();
                result.getExecution();
                httpServletResponse.sendRedirect("http://localhost:4200/verified");
            }
        }
    }
}

