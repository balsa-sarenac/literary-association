package upp.team5.literaryassociation.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.exception.UserAlreadyExistsException;
import upp.team5.literaryassociation.security.service.RegistrationService;
import upp.team5.literaryassociation.security.dto.FormFieldsDTO;
import upp.team5.literaryassociation.security.dto.FormSubmissionDTO;
import upp.team5.literaryassociation.security.dto.FormSubmissionFieldDTO;
import upp.team5.literaryassociation.security.dto.RegistrationDTO;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/auth")
public class RegistrationController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;

    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
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
        runtimeService.setVariable(processInstanceId, "author-register-data", map);

        formService.submitTaskForm(taskId, map);
    }

    private HashMap<String, Object> listToMap(List<FormSubmissionFieldDTO> formSubmissionDTOS) {
        HashMap<String, Object> map = new HashMap<>();
        for (FormSubmissionFieldDTO fs : formSubmissionDTOS) {
            map.put(fs.getId(), fs.getValue());
        }
        return map;
    }
}

