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
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.exception.UserAlreadyExistsException;
import upp.team5.literaryassociation.security.dto.FormFieldsDTO;
import upp.team5.literaryassociation.security.dto.FormSubmissionDTO;
import upp.team5.literaryassociation.security.dto.FormSubmissionFieldDTO;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
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

    @PostMapping(name = "submitRegistrationForm", path = "/submitForm/{taskId}")
    public ResponseEntity<?> submitDocuments(@RequestBody FormSubmissionDTO formSubmissionDTO, @PathVariable String taskId) throws UserAlreadyExistsException {
        log.info("Membership form submitted");
        HashMap<String, Object> regFormData = this.listToMap(formSubmissionDTO.getFormFields());

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "submitted-documents", regFormData);

        formService.submitTaskForm(taskId, regFormData);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    private HashMap<String, Object> listToMap(List<FormSubmissionFieldDTO> formSubmissionDTOS) {
        HashMap<String, Object> map = new HashMap<>();
        for (FormSubmissionFieldDTO fs : formSubmissionDTOS) {
            map.put(fs.getId(), fs.getValue());
        }
        return map;
    }
}
