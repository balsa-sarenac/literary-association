package upp.team5.literaryassociation.authorRegistration.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

public class RequestMoreDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long membershipId = (long) delegateExecution.getVariable("membershipRequestId");
        User user = this.userRepository.findByMembershipRequest_Id(membershipId);

        String to = user.getEmail();
        String subject = "We need more manuscripts";
        String body = "Hello!\n\n";
        body += "Some of our committee members requested more material to decide upon your registration request.";
        body += "Please log int to your profile and submit more documents. You have 2 weeks to do so, after that your request will be terminated.";

        this.emailService.Send(to, body, subject);
    }
}
