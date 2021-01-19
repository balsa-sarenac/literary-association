package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;

@Service @Slf4j
public class NotifyStatusLossDelegate implements JavaDelegate {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public NotifyStatusLossDelegate(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Getting beta user");
        Long betaUserId = (Long) delegateExecution.getVariable("betaReader");
        User betaUser = this.userRepository.findById(betaUserId)
                .orElseThrow(() -> new NotFoundException("User with given id doesn't exist"));

        log.info("Sending mail to beta reader");
        emailService.Send(betaUser.getEmail(), "You have 5 penalty points for not reading early access books," +
                " therefore you've lost beta reader status. You only have reader status now.",
                "You've lost beta reader status");
    }
}
