package upp.team5.literaryassociation.register.service;

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
import upp.team5.literaryassociation.form.dto.FormSubmissionFieldDTO;
import upp.team5.literaryassociation.model.Genre;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.register.dto.RegistrationDTO;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.*;

@Service
@Slf4j
public class RegistrationService implements JavaDelegate {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private GenreService genreService;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws UserAlreadyExistsException {
        log.info("Registration - user create initiated");

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-basic-information");
        HashMap<String, Object> additionalGenres = (HashMap<String, Object>) delegateExecution.getVariable("data-additional-genres");

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

            SetGenres(formSubmission, user);

            Set<Role> rolesSet = new HashSet<>();
            var isBeta = Boolean.parseBoolean(formSubmission.get("isBetaReader").toString());
            if(isBeta) {
                rolesSet.add(roleRepository.findByName("ROLE_BETA_READER"));
                SetGenres(additionalGenres, user);
            }
            else
                rolesSet.add(roleRepository.findByName("ROLE_READER"));
            user.setRoles(rolesSet);



            this.userRepository.save(user);
        }


    }

    private void SetGenres(HashMap<String, Object> formSubmission, User user) {
        Set<Genre> myGenres = new HashSet<>();
        var selected = (List<String>)formSubmission.get("genres");
        for (String genre : selected) {
            Genre g = genreService.getGenreByName(genre);
            if(g!= null)
                myGenres.add(g);
        }
        user.setGenres(myGenres);
    }
}
