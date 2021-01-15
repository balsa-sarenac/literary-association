package upp.team5.literaryassociation.authorMembershipPayment.delegate;

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
public class PaymentTimeout implements JavaDelegate {
    @Autowired
    private EmailService emailService;

    @Autowired
    private MembershipRequestService membershipRequestService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        long membershipId = (long) execution.getVariable("membershipRequestId");
        MembershipRequest membershipRequest = this.membershipRequestService.getMembershipRequest(membershipId);
        User user = this.userRepository.findByMembershipRequest(membershipRequest);

        membershipRequest.setActive(false);
        user.setEnabled(false);

        String to = user.getEmail();
        String subject = "Payment timeout!";
        String body = "We would like to inform you that your time to pay membership request has expired.";

        this.emailService.Send(to, body, subject);
    }
}
