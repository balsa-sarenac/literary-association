package upp.team5.literaryassociation.authorRegistration.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.model.MembershipRequest;

@Service @Slf4j
public class UpdateRequestDelegate implements JavaDelegate {

    @Autowired
    private MembershipRequestService membershipRequestService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long requestId = (long) delegateExecution.getVariable("membershipRequestId");
        MembershipRequest membershipRequest = this.membershipRequestService.getMembershipRequest(requestId);

        log.info("Updating voting round number");
        membershipRequest.setVoteRound(membershipRequest.getVoteRound() + 1);
        membershipRequestService.save(membershipRequest);

    }
}
