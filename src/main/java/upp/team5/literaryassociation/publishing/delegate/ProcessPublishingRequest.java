package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProcessPublishingRequest implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Processing publishing request initialized");

        log.info("process 2 created");
        String corrId = (String) delegateExecution.getVariable("publishingCorrelationId");
//        delegateExecution.getProcessEngineServices()
//                .getRuntimeService()
//                .createMessageCorrelation("responsePublishing")
//                .processInstanceBusinessKey(corrId.toString())
//                .correlateWithResult();

        delegateExecution.getProcessEngineServices()
                .getRuntimeService()
                .createMessageCorrelation("responsePublishing")
                .processInstanceId(corrId)
                .correlateWithResult();
        log.info("Processing publishing request done");
    }
}
