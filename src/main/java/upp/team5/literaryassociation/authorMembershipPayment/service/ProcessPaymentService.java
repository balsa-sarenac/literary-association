package upp.team5.literaryassociation.authorMembershipPayment.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessPaymentService implements JavaDelegate {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private MembershipRequestService membershipRequestService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        runtimeService.signalEventReceived("PaymentReceived");

        String processId = execution.getProcessInstanceId();

        Long id = (long)runtimeService.getVariable(processId, "membershipRequestId");

        MembershipRequest membershipRequest = membershipRequestService.getMembershipRequest(id);

        membershipRequest.setFeePaid(true);

        User user = membershipRequest.getAuthor();

        List<Role> userRole = user.getRoles().stream().filter(role-> role.equals(roleRepository.findByName("ROLE_PENDING_AUTHOR"))).collect(Collectors.toList());
        for(Role role : userRole){
            user.getRoles().remove(role);
        }

        user.getRoles().add(roleRepository.findByName("ROLE_AUTHOR"));

        userRepository.save(user);

    }
}
