package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.security.dto.FormFieldsDTO;
import upp.team5.literaryassociation.security.dto.FormSubmissionDTO;
import upp.team5.literaryassociation.security.dto.FormSubmissionFieldDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

//@CrossOrigin("*")
@RestController @Slf4j
@RequestMapping(produces = "application/json", path = "/auth")
public class UserController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;

    private CustomUserDetailsService userDetailsService;
    private LoginService loginService;

    @Autowired
    public UserController(CustomUserDetailsService userDetailsService, LoginService loginService) {
        this.userDetailsService = userDetailsService;
        this.loginService = loginService;
    }


    @GetMapping(name = "getLoginForm", path = "/form-login")
    public ResponseEntity<FormFieldsDTO> loginForm() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("login-process");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        FormFieldsDTO formFieldsDTO = new FormFieldsDTO(processInstance.getId(), task.getId(), properties);

        return new ResponseEntity<>(formFieldsDTO, HttpStatus.OK);
    }

    @PostMapping(name = "login", path = "/login/{taskId}")
    public void login(@RequestBody FormSubmissionDTO formSubmissionDTO, @PathVariable String taskId,
                      HttpServletRequest httpServletRequest)  throws AuthenticationException {
        log.info("Initialising login functionality");
        HashMap<String, Object> map = this.listToMap(formSubmissionDTO.getFormFields());

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "login-data", map);
//        runtimeService.setVariable(processInstanceId, "login-request", httpServletRequest);
        formService.submitTaskForm(taskId, map);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(name = "enable", path = "/enable/{userId}")
    public ResponseEntity<?> enable(@PathVariable Long userId) {
        return this.userDetailsService.enable(userId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(name = "disable", path = "/disable/{userId}")
    public ResponseEntity<?> disable(@PathVariable Long userId) {
        return this.userDetailsService.disable(userId);
    }


    private HashMap<String, Object> listToMap(List<FormSubmissionFieldDTO> formSubmissionDTOS) {
        HashMap<String, Object> map = new HashMap<>();
        for (FormSubmissionFieldDTO fs : formSubmissionDTOS) {
            map.put(fs.getId(), fs.getValue());
        }
        return map;
    }
}
