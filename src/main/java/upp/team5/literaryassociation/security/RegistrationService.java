package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.exception.UserAlreadyExistsException;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.dto.RegistrationDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class RegistrationService implements JavaDelegate {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> register(RegistrationDTO regDTO) {
        var u = userRepository.getUserByEmail(regDTO.getEmail());
        if(u != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setEnabled(false);
        user.setCity(regDTO.getCity());
        user.setCountry(regDTO.getCountry());
        user.setEmail(regDTO.getEmail());
        user.setFirstName(regDTO.getFirstName());
        user.setLastName(regDTO.getLastName());
        user.setPassword(passwordEncoder.encode(regDTO.getPassword()));

        Set<Role> rolesSet = new HashSet<>();
        var role = roleRepository.findByName(regDTO.getRole());
        rolesSet.add(role);
        user.setRoles(rolesSet);
        
        this.userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws UserAlreadyExistsException {
        log.info("Registration - user create initiated");

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("register-data");

        var u = userRepository.getUserByEmail(formSubmission.get("email").toString());
        if(u != null) {
            runtimeService.setVariable(delegateExecution.getProcessInstanceId(), "userExists", true);
            throw new UserAlreadyExistsException("User with the same email address already exists.");
        }
        else {
            runtimeService.setVariable(delegateExecution.getProcessInstanceId(), "userExists", false);

            User user = new User();
            user.setEnabled(false);
            user.setCity(formSubmission.get("city").toString());
            user.setCountry(formSubmission.get("country").toString());
            user.setEmail(formSubmission.get("email").toString());
            user.setFirstName(formSubmission.get("firstName").toString());
            user.setLastName(formSubmission.get("lastName").toString());
            user.setPassword(passwordEncoder.encode(formSubmission.get("password").toString()));
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);

            Set<Role> rolesSet = new HashSet<>();
            var isBeta = Boolean.parseBoolean(formSubmission.get("isBetaReader").toString());
            if(isBeta)
                rolesSet.add(roleRepository.findByName("ROLE_BETA_READER"));
            else
                rolesSet.add(roleRepository.findByName("ROLE_READER"));
            user.setRoles(rolesSet);

            this.userRepository.save(user);
        }


    }
}
