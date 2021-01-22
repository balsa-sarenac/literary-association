package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class NotificationService implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;
    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("send email to notify author about refusal");
        var requestId = execution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));
        String explanation = execution.getVariable("explanation").toString();
        if(request.isPresent()){
            var pubReq = request.get();
            Set<User> authors = pubReq.getBook().getAuthors();
            for(User author:authors){
                String to=author.getEmail();
                String subject = "Refused";
                String body = "Hello, we are sorry to inform you that your publishing request is refused.";
                body+="Explanation: ";
                body+=explanation;
                emailService.Send(to, body, subject);
            }

        }
    }
}
