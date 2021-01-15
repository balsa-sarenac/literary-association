package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class InitiatePlagiarismCheck implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Sending start message to plagiarism check process");
        delegateExecution.getProcessEngineServices()
                .getRuntimeService()
                .createMessageCorrelation("StartPlagiarismCheck")
                .setVariable("correlationId", delegateExecution.getProcessInstance().getProcessBusinessKey())
                .correlateWithResult();
    }
}
