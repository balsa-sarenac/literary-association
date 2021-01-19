package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;
import upp.team5.literaryassociation.security.repository.UserRepository;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UploadTimeoutDelegate implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;
    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        long publishingRequestId = (long) execution.getVariable("publishing-request-id");
        PublishingRequest publishingRequest = this.publishingRequestService.getPublishingRequest(publishingRequestId);
        Set<User> authors = publishingRequest.getBook().getAuthors();

        publishingRequest.setApproved(false);
        publishingRequest.setStatus("Book upload timeout");
        publishingRequestService.savePublishingRequest(publishingRequest);

        for(User author:authors){
            String to = author.getEmail();
            String subject = "Book upload timeout";
            String body = "We are sorry to inform you that your time to upload book has expired.";


            this.emailService.Send(to, body, subject);
        }


    }
}
