package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InitiatePublishing implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Sending start message to publishing process");
        delegateExecution.getProcessEngineServices()
                .getRuntimeService()
                .createMessageCorrelation("requestPublishing")
                .setVariable("publishingCorrelationId", delegateExecution.getProcessInstanceId())
                .setVariable("publishing-request-id", delegateExecution.getVariable("publishing-request-id"))
                .correlateWithResult();
    }
}
