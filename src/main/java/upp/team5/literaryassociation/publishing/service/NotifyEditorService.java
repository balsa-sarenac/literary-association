package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.PublishingRequest;

import java.util.List;

@Service
@Slf4j
public class NotifyEditorService implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;
    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("send email to notify chief editor");
        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));
        if(request.isPresent()){
            var pubReq = request.get();
            String to = pubReq.getBook().getChiefEditor().getEmail();
            String subject = "New publishing request";
            String body = "There was a new publishing request waiting for review.";
            emailService.Send(to, body, subject);
        }
    }
}
