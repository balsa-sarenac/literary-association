package upp.team5.literaryassociation.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;

@Service @Slf4j
public class AuthUserService {

    private final UserRepository userRepository;

    @Autowired
    public AuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedInUser() {
        log.info("Getting logged in user");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if(principal instanceof UserDetails){
            email = ((UserDetails)principal).getUsername();
        }
        else{
            email = principal.toString();
        }

        return this.userRepository.findByEmail(email);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with given id doesn't exist"));
    }
}
