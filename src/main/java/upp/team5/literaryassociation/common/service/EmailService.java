package upp.team5.literaryassociation.common.service;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.VerificationInformation;
import upp.team5.literaryassociation.register.repository.VerificationInformationRepository;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
@Slf4j
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    private VerificationInformationRepository verificationInformationRepository;

    @Autowired
    public EmailService(VerificationInformationRepository verificationInformationRepository) {
        this.verificationInformationRepository = verificationInformationRepository;
    }

    public void Send(String to, String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
        log.info("Email sent");
    }
}
