package upp.team5.literaryassociation.register.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.HashMap;

@Service
@Slf4j
public class VerificationService implements JavaDelegate {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdentityService identityService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Initiating account verification");

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-basic-information");

        var user = userRepository.getUserByEmail(formSubmission.get("email").toString());
        user.setEnabled(true);

        user = userRepository.save(user);
        this.createCamundaUser(user, "asdf");
        log.info("Account verified");
    }


    /**
     * Creates user and saves it in camunda identity service
     * @param user User object that is saved to db
     * @param password users password since its hashed in User object
     */
    public void createCamundaUser(User user, String password) {
        org.camunda.bpm.engine.identity.User cUser = identityService.newUser(user.getId().toString());
        cUser.setEmail(user.getEmail());
        cUser.setFirstName(user.getFirstName());
        cUser.setLastName(user.getLastName());
        cUser.setPassword(password);
        identityService.saveUser(cUser);
    }
}
