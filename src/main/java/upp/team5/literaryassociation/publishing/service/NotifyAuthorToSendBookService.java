package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;

@Service
@Slf4j
public class NotifyAuthorToSendBookService implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;
    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("send email to author to upload book");
        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));
        if(request.isPresent()){
            var pubReq = request.get();
            var author = pubReq.getBook().getAuthors().stream().findFirst();
            if(author.isPresent()){
                var a = author.get();
                String to = a.getEmail();
                String subject = "Please upload book for revision";
                String body = "Chief editor would like to read your book, please log in to your account and upload whole book for revision.";
                emailService.Send(to, body, subject);
            }
        }
    }
}
