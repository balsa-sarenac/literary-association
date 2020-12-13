package upp.team5.literaryassociation.membershipApplication.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upp.team5.literaryassociation.security.dto.FormFieldsDTO;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/membership")
public class MembershipApplicationController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;

    @GetMapping(name = "getRegistrationForm", path = "/app-form")
    public ResponseEntity<FormFieldsDTO> getDocumentApplicationForm() {
        log.info("Application form requested, initiating writer document application process");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("author-application");
        Task regTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);

        TaskFormData taskFormData = formService.getTaskFormData(regTask.getId());
        List<FormField> fields = taskFormData.getFormFields();

        FormFieldsDTO formFieldsDTO = new FormFieldsDTO(processInstance.getId(), regTask.getId(), fields);
        return new ResponseEntity<>(formFieldsDTO, HttpStatus.OK);
    }
}
