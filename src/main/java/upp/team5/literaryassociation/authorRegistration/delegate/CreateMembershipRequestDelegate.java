package upp.team5.literaryassociation.authorRegistration.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;

@Service
@Slf4j
public class CreateMembershipRequestDelegate implements JavaDelegate {

    private MembershipRequestService membershipRequestService;

    @Autowired
    public CreateMembershipRequestDelegate(MembershipRequestService membershipRequestService) {
        this.membershipRequestService = membershipRequestService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        membershipRequestService.addNewRequest(delegateExecution);
    }
}
