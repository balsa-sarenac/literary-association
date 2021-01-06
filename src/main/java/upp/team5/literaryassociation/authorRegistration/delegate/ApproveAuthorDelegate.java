package upp.team5.literaryassociation.authorRegistration.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

@Service
public class ApproveAuthor implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRequestService membershipRequestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long membershipId = (long) delegateExecution.getVariable("membershipRequestId");
        MembershipRequest membershipRequest = this.membershipRequestService.getMembershipRequest(membershipId);
        User user = this.userRepository.findByMembershipRequest(membershipRequest);

        String to = user.getEmail();
        String subject = "You are approved!!!";
        String body = "Congratulations!\n\n";
        body += "You have been approved to join our literary association!";
        body += "Next step is paying membership fee. Please log in to your profile and pay to start using our site!";

        this.emailService.Send(to, body, subject);
    }
}
