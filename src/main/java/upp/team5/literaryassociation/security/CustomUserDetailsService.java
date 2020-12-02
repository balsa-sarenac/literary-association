package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.dto.JwtAuthenticationRequest;
import upp.team5.literaryassociation.security.dto.UserTokenState;
import upp.team5.literaryassociation.security.token.TokenUtils;

@Service @Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private TokenUtils tokenUtils;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, AuthenticationManager authenticationManager,
                                    TokenUtils tokenUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.userRepository.getUserByEmail(s);

        if (user == null) throw new UsernameNotFoundException("Could not find user with given username/email");

        return user;
    }

    public ResponseEntity<?> login(JwtAuthenticationRequest authenticationRequest) {
        log.info("Login function initialised");
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                                                        authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        String refresh = tokenUtils.generateRefreshToken(user.getUsername());
        String email = user.getUsername();
        String role = user.getRoles().iterator().next().getName();
        Long id = user.getId();

        return ResponseEntity.ok(new UserTokenState(id, jwt, expiresIn, refresh, email, role));
    }
}
