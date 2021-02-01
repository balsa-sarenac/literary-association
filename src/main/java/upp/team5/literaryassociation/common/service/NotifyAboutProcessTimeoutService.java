package upp.team5.literaryassociation.common.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.model.*;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

@Service
@Slf4j
public class NotifyAboutProcessTimeoutService implements JavaDelegate {
    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Autowired
    private PublishingRequestService publishingRequestService;

    @Autowired
    private MembershipRequestService membershipRequestService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long userId = (Long)execution.getVariable("assignee");

        if(userId != null){
            User loggedUser = userService.getUser(userId);

            if(loggedUser != null){
                String to = loggedUser.getEmail();
                String subject = "Process timeout";
                String body = "Hello,\n\nwe would like to inform you that your time to finish started process has finished!";

                emailService.Send(to, body, subject);

                if(execution.getProcessDefinitionId().split(":")[0].equals("plagiarism-process")){
                    Long id = (Long) execution.getVariable("plagiarism-complaint-id");
                    PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.getPlagiarismComplaint(id);
                    plagiarismComplaint.setPlagiarismComplaintStage(PlagiarismComplaintStage.TIMEOUT);
                    plagiarismComplaintService.save(plagiarismComplaint);
                }
                else if(execution.getProcessDefinitionId().split(":")[0].equals("book-publishing")){
                    Long id = (Long) execution.getVariable("publishing-request-id");
                    PublishingRequest publishingRequest = publishingRequestService.getPublishingRequest(id);
                    publishingRequest.setStatus("Timeout");
                    publishingRequestService.savePublishingRequest(publishingRequest);
                }
                else if(execution.getProcessDefinitionId().split(":")[0].equals("author-reg")){
                    Long id = (Long) execution.getVariable("membershipRequestId");
                    MembershipRequest membershipRequest = membershipRequestService.getMembershipRequest(id);
                    membershipRequest.setActive(false);
                    membershipRequestService.save(membershipRequest);
                }
            }

        }

    }
}
