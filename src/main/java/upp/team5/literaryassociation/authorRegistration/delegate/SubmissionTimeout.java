package upp.team5.literaryassociation.authorRegistration.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;
import upp.team5.literaryassociation.security.service.RoleService;

import java.util.HashSet;

@Service
public class SubmissionTimeout implements JavaDelegate {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MembershipRequestService membershipRequestService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        long membershipId = (long) execution.getVariable("membershipRequestId");
        MembershipRequest membershipRequest = this.membershipRequestService.getMembershipRequest(membershipId);
        User user = this.userRepository.findByMembershipRequest(membershipRequest);

        user.setRoles(new HashSet<>());

        membershipRequest.setActive(false);
        user.setEnabled(false);
        
        userRepository.save(user);

        String to = user.getEmail();
        String subject = "Submission timeout";
        String body = "We are sorry to inform you that your time to upload more documents has expired.";

        this.emailService.Send(to, body, subject);
    }
}
