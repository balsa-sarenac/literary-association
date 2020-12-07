package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * Accepts user authentication request and tries to log in user
     * @param authenticationRequest DTO with username (email) and password
     * @return OK if user is successfully logged in
     * @throws AuthenticationException If credentials are not correct
     */
    @PostMapping(name = "login", path = "/login")
    public ResponseEntity<?> login(@RequestBody JwtAuthenticationRequest authenticationRequest)  throws AuthenticationException {
        return this.userDetailsService.login(authenticationRequest);
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
