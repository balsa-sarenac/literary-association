package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class EmailService implements JavaDelegate {
    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Initiating sending email");

        SimpleMailMessage message = new SimpleMailMessage();

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("register-data");
        var firstName = formSubmission.get("firstName");

        message.setTo(formSubmission.get("email").toString());
        message.setSubject("Account verification");
        String messageText = "Hello, " + firstName + "!" + "\n\n";
        messageText += "You have recently registered on our literary association website.";
        messageText += "To verify your account, please click the following link: ";
        delegateExecution.setProcessBusinessKey(delegateExecution.getProcessInstanceId());
        messageText += "http://localhost:8080/auth/clickVerification/" + delegateExecution.getProcessInstanceId() + "\n\n";
        messageText += "If you did not register on our website, please ignore this message.";
        message.setText(messageText);
        javaMailSender.send(message);

        log.info("Email sent");
    }
}
