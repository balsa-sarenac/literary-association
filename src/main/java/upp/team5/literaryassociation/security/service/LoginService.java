package upp.team5.literaryassociation.security.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.dto.LoginRequestDTO;
import upp.team5.literaryassociation.security.dto.UserTokenState;
import upp.team5.literaryassociation.security.token.TokenUtils;

@Service @Slf4j
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    IdentityService identityService;
    private TokenUtils tokenUtils;

    @Autowired
    public LoginService(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    public ResponseEntity<UserTokenState> login(LoginRequestDTO loginRequestDTO) {
        log.info("Trying to authenticate");
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        String refresh = tokenUtils.generateRefreshToken(user.getUsername());
        String email = user.getUsername();
        String role = user.getRoles().iterator().next().getName();
        Long id = user.getId();
        String status = user.getStatus();

        log.info("Sending user his token");
        UserTokenState userTokenState = new UserTokenState(id, jwt, expiresIn, refresh, email, role, status);
        return new ResponseEntity<>(userTokenState, HttpStatus.OK);
    }
}
