package upp.team5.literaryassociation.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.register.dto.FormSubmissionFieldDTO;
import upp.team5.literaryassociation.security.dto.LoginRequestDTO;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;
import upp.team5.literaryassociation.security.service.LoginService;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
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

    @PostMapping(name = "login", path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO)  throws AuthenticationException {
        log.info("Initialising login functionality");
        return loginService.login(loginRequestDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(name = "getRequests", path = "/requests")
    public ResponseEntity<?> getRegistrationRequests() {return this.userDetailsService.getRegistrationRequests();}

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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(name = "delete", path = "/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable Long userId) {
        log.info("Calling service to delete given user");
        return this.userDetailsService.delete(userId); }

    private HashMap<String, Object> listToMap(List<FormSubmissionFieldDTO> formSubmissionDTOS) {
        HashMap<String, Object> map = new HashMap<>();
        for (FormSubmissionFieldDTO fs : formSubmissionDTOS) {
            map.put(fs.getId(), fs.getValue());
        }
        return map;
    }
}
