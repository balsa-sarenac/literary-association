package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.security.repository.UserRepository;

@Service
@Slf4j
public class ChooseEditorService implements JavaDelegate {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
