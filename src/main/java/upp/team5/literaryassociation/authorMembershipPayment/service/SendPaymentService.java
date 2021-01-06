package upp.team5.literaryassociation.authorMembershipPayment.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendPaymentService implements JavaDelegate {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("payment done");

        String processId = execution.getProcessInstanceId();
        MessageCorrelationResult result = runtimeService.createMessageCorrelation("PaymentReceived")
                .processInstanceId(processId)
                .correlateWithResult();
        result.getExecution();
    }
}
