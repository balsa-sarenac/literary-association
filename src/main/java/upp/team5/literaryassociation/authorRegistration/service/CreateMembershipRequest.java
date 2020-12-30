package upp.team5.literaryassociation.authorRegistration.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateMembershipRequest implements JavaDelegate {

    private MembershipRequestService membershipRequestService;

    @Autowired
    public CreateMembershipRequest(MembershipRequestService membershipRequestService) {
        this.membershipRequestService = membershipRequestService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        membershipRequestService.addNewRequest(delegateExecution);
    }
}
