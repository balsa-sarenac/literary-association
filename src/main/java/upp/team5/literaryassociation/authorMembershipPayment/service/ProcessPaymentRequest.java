package upp.team5.literaryassociation.authorMembershipPayment.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProcessPaymentRequest implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("process 2 created");
        var corrId = delegateExecution.getVariable("correlationId");
        delegateExecution.getProcessEngineServices()
                .getRuntimeService()
                .createMessageCorrelation("response")
                .processInstanceBusinessKey(corrId.toString())
                .correlateWithResult();
    }
}
