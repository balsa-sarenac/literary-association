package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.exception.UserAlreadyExistsException;
import upp.team5.literaryassociation.security.dto.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/auth")
public class RegistrationController {

    private RegistrationService registrationService;
    private VerificationInformationRepository verificationInformationRepository;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public RegistrationController(RegistrationService registrationService, VerificationInformationRepository verificationInformationRepository) {
        this.registrationService = registrationService;
        this.verificationInformationRepository = verificationInformationRepository;
    }

    @PostMapping(name = "register", path = "/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDTO registrationDTO) throws UserAlreadyExistsException {
        return registrationService.register(registrationDTO);
    }

    @GetMapping(name = "getRegistrationForm", path = "/form-registration")
    public ResponseEntity<FormFieldsDTO> getRegistrationForm() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("registration-process");
        Task regTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);

        TaskFormData taskFormData = formService.getTaskFormData(regTask.getId());
        List<FormField> fields = taskFormData.getFormFields();

        FormFieldsDTO formFieldsDTO = new FormFieldsDTO(processInstance.getId(), regTask.getId(), fields);
        return new ResponseEntity<>(formFieldsDTO, HttpStatus.OK);
    }

    @PostMapping(name = "submitRegistrationForm", path = "/submitRegForm/{taskId}")
    public ResponseEntity<?> submitRegistrationForm(@RequestBody FormSubmissionDTO formSubmissionDTO, @PathVariable String taskId) throws UserAlreadyExistsException {
        HashMap<String, Object> regFormData = new HashMap<>();
        for (FormSubmissionFieldDTO fs : formSubmissionDTO.getFormFields()) {
            regFormData.put(fs.getId(), fs.getValue());
        }

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "register-data", regFormData);
        var isBetaReader = Boolean.parseBoolean( regFormData.get("isBetaReader").toString());
        runtimeService.setVariable(processInstanceId, "isBetaReader", isBetaReader);

        formService.submitTaskForm(taskId, regFormData);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(name = "testSendingEmail", path = "/testEmail")
    public ResponseEntity<?> testSendingEmail(@RequestBody EmailDTO emailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDTO.getTo());
        message.setSubject(emailDTO.getTopic());
        message.setText(emailDTO.getBody());
        javaMailSender.send(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(name = "clickVerification", path = "/clickVerification/{email}/{hash}")
    public ResponseEntity<?> clickVerification(@PathVariable String email, @PathVariable String hash) {
        log.info("verification link clicked");

        var verInf = verificationInformationRepository.getVerificationInformationByHash(hash);
        if(verInf != null) {
            if(verInf.getEmail().equals(email)){
                MessageCorrelationResult result = runtimeService.createMessageCorrelation("LinkClicked")
                        .processInstanceBusinessKey(verInf.getProcessBussinessKey())
                        .setVariable("userVerified", true)
                        .correlateWithResult();
                var execution = result.getExecution();
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

