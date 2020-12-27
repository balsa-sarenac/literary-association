package upp.team5.literaryassociation.register.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class AuthorRegistrationService implements JavaDelegate {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;


    @Autowired
    AuthorRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-basic-information");

        boolean userExists;
        var u = userRepository.getUserByEmail(formSubmission.get("email").toString());
        if(u != null) {
            userExists = true;

        }
        else{
            userExists = false;

            User user = new User();
            user.setEnabled(false);
            user.setCity(formSubmission.get("city").toString());
            user.setCountry(formSubmission.get("country").toString());
            user.setEmail(formSubmission.get("email").toString());
            user.setFirstName(formSubmission.get("firstName").toString());
            user.setLastName(formSubmission.get("lastName").toString());
            user.setPassword(passwordEncoder.encode(formSubmission.get("password").toString()));

            Set<Role> rolesSet = new HashSet<Role>();
            var role = roleRepository.findByName("ROLE_PENDING_AUTHOR");
            rolesSet.add(role);
            user.setRoles(rolesSet);

            this.userRepository.save(user);
        }
        delegateExecution.setVariable("userExists", userExists);
    }
}
