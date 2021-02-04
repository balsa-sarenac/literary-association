package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.Set;

@Service
@Slf4j
public class EditingTimeout implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;


    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("editing timeout initialized");

        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));


        if(request.isPresent()) {
            PublishingRequest publishingRequest = request.get();

            publishingRequest.setStatus("Editing timeout happened");
            publishingRequestService.savePublishingRequest(publishingRequest);

            Set<User> authors = publishingRequest.getBook().getAuthors();
            for(User author:authors){
                String to = author.getEmail();
                String subject = "Editing timeout";
                String body = "We are sorry to inform you that your time to upload changed book after editor's suggestions has expired.";

                this.emailService.Send(to, body, subject);
            }
        }
    }
}
