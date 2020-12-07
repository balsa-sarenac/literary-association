package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.security.dto.JwtAuthenticationRequest;

import java.io.IOException;

//@CrossOrigin("*")
@RestController @Slf4j
@RequestMapping(produces = "application/json")
public class UserController {

    private CustomUserDetailsService userDetailsService;

    @Autowired
    public UserController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(name = "login", path = "/login")
    public ResponseEntity<?> login(@RequestBody JwtAuthenticationRequest authenticationRequest)  throws AuthenticationException {
        return this.userDetailsService.login(authenticationRequest);
    }

    @GetMapping(name = "enable", path = "/enable/{userId}")
    public ResponseEntity<?> enable(@PathVariable Long userId) {
        return this.userDetailsService.enable(userId);
    }

}
