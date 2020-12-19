package upp.team5.literaryassociation.register.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.HashMap;

@Service
@Slf4j
public class VerificationService implements JavaDelegate {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IdentityService identityService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Initiating account verification");

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data");

        var user = userRepository.getUserByEmail(formSubmission.get("email").toString());
        user.setEnabled(true);
        userRepository.save(user);

        delegateExecution.setProcessBusinessKey(user.getEmail());
//
//        User verified = identityService.newUser(user.getEmail());
//        verified.setPassword(user.getPassword());
//        verified.setEmail(user.getEmail());
//        runtimeService.setVariable(delegateExecution.getProcessInstanceId(), "verifiedUser", verified);

        log.info("Account verified");
    }
}
