package upp.team5.literaryassociation.security.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.HashMap;

@Service
@Slf4j
public class VerificationService implements JavaDelegate {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Initiating account verification");

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("register-data");

        var user = userRepository.getUserByEmail(formSubmission.get("email").toString());
        user.setEnabled(true);
        userRepository.save(user);

        log.info("Account verified");
    }
}
