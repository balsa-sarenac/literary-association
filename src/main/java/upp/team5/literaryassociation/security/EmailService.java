package upp.team5.literaryassociation.security;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.VerificationInformation;
import upp.team5.literaryassociation.security.repository.VerificationInformationRepository;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
@Slf4j
public class EmailService implements JavaDelegate {
    @Autowired
    JavaMailSender javaMailSender;

    private VerificationInformationRepository verificationInformationRepository;

    @Autowired
    public EmailService(VerificationInformationRepository verificationInformationRepository) {
        this.verificationInformationRepository = verificationInformationRepository;
    }


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Initiating sending email");

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("register-data");
        delegateExecution.setProcessBusinessKey(delegateExecution.getProcessInstanceId());
        VerificationInformation verInf = new VerificationInformation();
        verInf.setProcessBusinessKey(delegateExecution.getProcessInstanceId());
        verInf.setEmail(formSubmission.get("email").toString());
        var hashCode = Hashing.sha256()
                .hashString(verInf.getProcessBusinessKey(), StandardCharsets.UTF_8)
                .toString();
        verInf.setHash(hashCode);
        verificationInformationRepository.save(verInf);

        var firstName = formSubmission.get("firstName");

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(formSubmission.get("email").toString());
        message.setSubject("Account verification");
        String messageText = "Hello, " + firstName + "!" + "\n\n";
        messageText += "You have recently registered on our literary association website.";
        messageText += "To verify your account, please click the following link: ";
        delegateExecution.setProcessBusinessKey(delegateExecution.getProcessInstanceId());
        messageText += "http://localhost:8080/auth/clickVerification/" + formSubmission.get("email").toString() + "/" + hashCode  + "\n\n";
        messageText += "If you did not register on our website, please ignore this message.";
        message.setText(messageText);
        javaMailSender.send(message);

        log.info("Email sent");
    }
}
