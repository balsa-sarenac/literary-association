package upp.team5.literaryassociation.authorMembershipPayment.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorMembershipPayment.controller.MembershipPaymentController;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

@Service
@Slf4j
public class SendPaymentService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getBusinessKey();

        execution.getProcessEngineServices()
                .getRuntimeService()
                .createMessageCorrelation("PaymentReceived")
                .processInstanceBusinessKey(businessKey)
                .setVariable("paid",true)
                .correlateWithResult();
    }

}
