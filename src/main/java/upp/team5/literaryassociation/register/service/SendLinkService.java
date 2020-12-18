package upp.team5.literaryassociation.register.service;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.VerificationInformation;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
@Slf4j
public class SendLinkService implements JavaDelegate {

    private VerificationInformationService verificationInformationService;
    private EmailService emailService;

    @Autowired
    public SendLinkService(VerificationInformationService verificationInformationService, EmailService emailService) {
        this.verificationInformationService = verificationInformationService;
        this.emailService = emailService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Initiating sending email");

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data");

        String businessKey = delegateExecution.getProcessInstanceId();
        delegateExecution.setProcessBusinessKey(businessKey);

        var hashCode = Hashing.sha256()
                .hashString(businessKey, StandardCharsets.UTF_8)
                .toString();

        VerificationInformation verInf = new VerificationInformation();
        verInf.setProcessBusinessKey(delegateExecution.getProcessInstanceId());
        verInf.setEmail(formSubmission.get("email").toString());
        verInf.setHash(hashCode);
        verificationInformationService.AddNew(verInf);

        String to = formSubmission.get("email").toString();
        String subject = "Account verification";
        String firstName = formSubmission.get("firstName").toString();
        String messageText = "Hello, " + firstName + "!" + "\n\n";
        messageText += "You have recently registered on our literary association website.";
        messageText += "To verify your account, please click the following link: ";
        messageText += "http://localhost:8080/auth/clickVerification/" + formSubmission.get("email").toString() + "/" + hashCode  + "\n\n";
        messageText += "If you did not register on our website, please ignore this message.";

        emailService.Send(to, messageText, subject);

        log.info("Email sent");
    }

}
