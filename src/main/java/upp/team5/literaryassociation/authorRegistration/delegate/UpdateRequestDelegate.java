package upp.team5.literaryassociation.authorRegistration.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

@Service @Slf4j
public class UpdateRequestDelegate implements JavaDelegate {

    @Autowired
    private MembershipRequestService membershipRequestService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long requestId = (long) delegateExecution.getVariable("membershipRequestId");
        MembershipRequest membershipRequest = membershipRequestService.getMembershipRequest(requestId);

        log.info("Updating voting round number");
        membershipRequest.setVoteRound(membershipRequest.getVoteRound() + 1);
        membershipRequestService.save(membershipRequest);

        User user = authUserService.getUserById((Long) delegateExecution.getVariable("authorId"));
        user.setStatus("reviewExpected");
        userRepository.save(user);
    }
}
