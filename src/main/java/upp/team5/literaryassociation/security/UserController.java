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
import upp.team5.literaryassociation.security.dto.JwtAuthenticationRequest;

import java.io.IOException;
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("Process_0blau3z");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        FormFieldsDTO formFieldsDTO = new FormFieldsDTO(processInstance.getId(), task.getId(), properties);

        return new ResponseEntity<>(formFieldsDTO, HttpStatus.OK);
    }

    @PostMapping(name = "login", path = "/login")
    public ResponseEntity<?> login(@RequestBody JwtAuthenticationRequest authenticationRequest)  throws AuthenticationException {
        log.info("Initialising login functionality");
        return this.loginService.login(authenticationRequest);
    }

    /**
     * If user is admin this endpoint will try to enable user with given id
     * @param userId Id of the user to be enabled
     * @throws upp.team5.literaryassociation.exception.UserNotFoundException if user is not found
     * @throws upp.team5.literaryassociation.exception.BadRequestException if user is already enabled
     * @return OK if user is enabled
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(name = "enable", path = "/enable/{userId}")
    public ResponseEntity<?> enable(@PathVariable Long userId) {
        return this.userDetailsService.enable(userId);
    }

    /**
     * If user is admin this endpoint will try to disable user with given id
     * @param userId Id of the user to be enabled
     * @throws upp.team5.literaryassociation.exception.UserNotFoundException if user is not found
     * @throws upp.team5.literaryassociation.exception.BadRequestException if user is already disabled
     * @return OK if user is disabled
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(name = "disable", path = "/disable/{userId}")
    public ResponseEntity<?> disable(@PathVariable Long userId) {
        return this.userDetailsService.disable(userId);
    }

}
