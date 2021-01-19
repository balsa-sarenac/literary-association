package upp.team5.literaryassociation.common.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

@Service
@Slf4j
public class NotifyAboutProcessTimeoutService implements JavaDelegate {
    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long userId = (Long)execution.getVariable("assignee");

        User loggedUser = userService.getUser(userId);

        String to = loggedUser.getEmail();
        String subject = "Process timeout";
        String body = "Hello,\n\nwe would like to inform you that your time to finish started process has finished!";

        emailService.Send(to, body, subject);
    }
}
