package upp.team5.literaryassociation.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.exception.UserAlreadyExistsException;
import upp.team5.literaryassociation.model.Genre;
import upp.team5.literaryassociation.security.repository.GenreRepository;
import upp.team5.literaryassociation.security.repository.VerificationInformationRepository;
import upp.team5.literaryassociation.security.dto.FormFieldsDTO;
import upp.team5.literaryassociation.security.dto.FormSubmissionDTO;
import upp.team5.literaryassociation.security.dto.FormSubmissionFieldDTO;
import upp.team5.literaryassociation.security.dto.RegistrationDTO;
import upp.team5.literaryassociation.security.service.RegistrationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/auth")
public class RegistrationController {

    private RegistrationService registrationService;
    private VerificationInformationRepository verificationInformationRepository;
    private GenreRepository genreRepository;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;

    @Autowired
    public RegistrationController(RegistrationService registrationService, VerificationInformationRepository verificationInformationRepository, GenreRepository genreRepository) {
        this.registrationService = registrationService;
        this.verificationInformationRepository = verificationInformationRepository;
        this.genreRepository = genreRepository;
    }

    @PostMapping(name = "register", path = "/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDTO registrationDTO) throws UserAlreadyExistsException {
        return registrationService.register(registrationDTO);
    }

    @GetMapping(name = "authorRegForm", path="/author-reg-form")
    public ResponseEntity<FormFieldsDTO> authorRegFrom() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("author-reg");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        FormFieldsDTO formFieldsDTO = new FormFieldsDTO(processInstance.getId(), task.getId(), properties);

        return new ResponseEntity<>(formFieldsDTO, HttpStatus.OK);
    }

    @PostMapping(name = "register-author", path = "/register-author/{taskId}")
    public void registerAuthor(@RequestBody FormSubmissionDTO formSubmissionDTO, @PathVariable String taskId){
        log.info("Initialising author register functionality");
        HashMap<String, Object> map = this.listToMap(formSubmissionDTO.getFormFields());

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "register-data", map);

        formService.submitTaskForm(taskId, map);
    }

    @GetMapping(name = "getRegistrationForm", path = "/form-registration")
    public ResponseEntity<FormFieldsDTO> getRegistrationForm() {
        log.info("Registration form requested, initiating reader registration process");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("registration-process");
        Task regTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);

        List<Genre> genres = genreRepository.findAll();

        TaskFormData taskFormData = formService.getTaskFormData(regTask.getId());
        List<FormField> fields = taskFormData.getFormFields();

        for(FormField field : fields){
            if(field.getId().equals("genres")){
                Map<String, String> enumType = ((EnumFormType) field.getType()).getValues();
                enumType.clear();
                for(Genre g: genres){
                    enumType.put(g.getName(), g.getName());
                }
            }
        }

        FormFieldsDTO formFieldsDTO = new FormFieldsDTO(processInstance.getId(), regTask.getId(), fields);
        return new ResponseEntity<>(formFieldsDTO, HttpStatus.OK);
    }

    @PostMapping(name = "submitRegistrationForm", path = "/submitRegForm/{taskId}")
    public ResponseEntity<?> submitRegistrationForm(@RequestBody FormSubmissionDTO formSubmissionDTO, @PathVariable String taskId) throws UserAlreadyExistsException {
        log.info("Registration form submitted");
        HashMap<String, Object> regFormData = this.listToMap(formSubmissionDTO.getFormFields());
        var isBetaReader = Boolean.parseBoolean( regFormData.get("isBetaReader").toString());

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "register-data", regFormData);
        runtimeService.setVariable(processInstanceId, "isBetaReader", isBetaReader);

        formService.submitTaskForm(taskId, regFormData);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(name = "clickVerification", path = "/clickVerification/{email}/{hash}")
    public ResponseEntity<?> clickVerification(@PathVariable String email, @PathVariable String hash) {
        log.info("Verification link clicked");

        var verInf = verificationInformationRepository.getVerificationInformationByHash(hash);
        if(verInf != null) {
            if(verInf.getEmail().equals(email)){
                MessageCorrelationResult result = runtimeService.createMessageCorrelation("LinkClicked")
                        .processInstanceBusinessKey(verInf.getProcessBusinessKey())
                        .setVariable("userVerified", true)
                        .correlateWithResult();
                result.getExecution();
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private HashMap<String, Object> listToMap(List<FormSubmissionFieldDTO> formSubmissionDTOS) {
        HashMap<String, Object> map = new HashMap<>();
        for (FormSubmissionFieldDTO fs : formSubmissionDTOS) {
            map.put(fs.getId(), fs.getValue());
        }
        return map;
    }

}

