package upp.team5.literaryassociation.authorRegistration.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

public class RejectAuthor implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long membershipId = (long) delegateExecution.getVariable("membershipRequestId");
        User user = this.userRepository.findByMembershipRequest_Id(membershipId);

        String to = user.getEmail();
        String subject = "Refusal";
        String body = "Your request to join our site has been refused by committee members!";

        this.emailService.Send(to, body, subject);
    }
}
