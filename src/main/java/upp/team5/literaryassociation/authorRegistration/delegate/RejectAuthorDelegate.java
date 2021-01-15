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
public class RejectAuthorDelegate implements JavaDelegate {

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

        membershipRequest.setActive(false);
        this.membershipRequestService.save(membershipRequest);

        String to = user.getEmail();
        String subject = "Refusal";
        String body = "Your request to join our site has been refused by committee members!";

        this.emailService.Send(to, body, subject);
    }
}
