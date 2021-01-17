package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.User;

import java.util.HashMap;
import java.util.Set;

@Service
@Slf4j
public class NotificationPlagiarismService implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;
    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("send email to notify author about refusal");
        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));
        HashMap<String, Object> explanation = (HashMap<String, Object>) delegateExecution.getVariable("data-plagiarism-explanation");
        if(request.isPresent()){
            var pubReq = request.get();
            Set<User> authors = pubReq.getBook().getAuthors();
            for(User author:authors){
                String to=author.getEmail();
                String subject = "Refused";
                String body = "Hello, we are sorry to inform you that your publishing request is refused because similar sources were found.";
                body+="Explanation: ";
                body+=explanation.get("explanation");
                emailService.Send(to, body, subject);
            }

        }
    }
}
