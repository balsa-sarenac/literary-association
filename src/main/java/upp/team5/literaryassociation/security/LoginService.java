package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.dto.FormSubmissionDTO;
import upp.team5.literaryassociation.security.dto.UserTokenState;
import upp.team5.literaryassociation.security.token.TokenUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service @Slf4j
public class LoginService implements JavaDelegate {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    IdentityService identityService;
    private TokenUtils tokenUtils;

    @Autowired
    public LoginService(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Delegating login execution");

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) execution.getVariable("login-data");


        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(formSubmission.get("username"),
                        formSubmission.get("password")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        String refresh = tokenUtils.generateRefreshToken(user.getUsername());
        String email = user.getUsername();
        String role = user.getRoles().iterator().next().getName();
        Long id = user.getId();
        UserTokenState userTokenState = new UserTokenState(id, jwt, expiresIn, refresh, email, role);

    }
}
