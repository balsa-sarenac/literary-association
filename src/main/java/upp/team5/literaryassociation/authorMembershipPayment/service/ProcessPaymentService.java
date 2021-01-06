package upp.team5.literaryassociation.authorMembershipPayment.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class ProcessPaymentService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        
    }
}
