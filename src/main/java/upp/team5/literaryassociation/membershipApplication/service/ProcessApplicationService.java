package upp.team5.literaryassociation.membershipApplication.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProcessApplicationService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
      log.info("Process application service started");
    }
}
