package upp.team5.literaryassociation.authorRegistration.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

@Service @Slf4j
public class RequestMoreDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRequestService membershipRequestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Getting request and user");
        long membershipId = (long) delegateExecution.getVariable("membershipRequestId");
        MembershipRequest membershipRequest = this.membershipRequestService.getMembershipRequest(membershipId);
        User user = this.userRepository.findByMembershipRequest(membershipRequest);

        String to = user.getEmail();
        String subject = "We need more manuscripts";
        String body = "Hello!\n\n";
        body += "Some of our committee members requested more material to decide upon your registration request.";
        body += "Please log int to your profile and submit more documents. You have 2 weeks to do so, after that your request will be terminated.";

        log.info("Sending mail to author to send more material");
        this.emailService.Send(to, body, subject);
    }
}
