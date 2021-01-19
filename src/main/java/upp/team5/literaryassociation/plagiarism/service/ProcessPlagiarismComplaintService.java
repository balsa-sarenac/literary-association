package upp.team5.literaryassociation.plagiarism.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProcessPlagiarismComplaintService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        
    }
}
