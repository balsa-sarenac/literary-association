package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;

@Service @Slf4j
public class GivePenaltyPointDelegate implements JavaDelegate {

    private final UserRepository userRepository;

    @Autowired
    public GivePenaltyPointDelegate(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Getting beta user");
        Long betaUserId = (Long) delegateExecution.getVariable("betaReader");
        User betaUser = this.userRepository.findById(betaUserId)
                .orElseThrow(() -> new NotFoundException("User with given id doesn't exist"));

        betaUser.setPenaltyPoints(betaUser.getPenaltyPoints() + 1);

        log.info("Setting process variable");
        delegateExecution.setVariable("penaltyPoints", betaUser.getPenaltyPoints());
    }
}
