package upp.team5.literaryassociation.security;

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
import upp.team5.literaryassociation.security.dto.FormFieldsDTO;
import upp.team5.literaryassociation.security.dto.RegistrationDTO;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/auth")
public class RegistrationController {

    private RegistrationService registrationService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
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

}

