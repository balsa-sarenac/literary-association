package upp.team5.literaryassociation.publishing.betaReader.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;

@Service @Slf4j
public class RemoveBetaStatusDelegate implements JavaDelegate {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public RemoveBetaStatusDelegate(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Getting beta user");
        org.camunda.bpm.engine.identity.User betaUserCamunda = (org.camunda.bpm.engine.identity.User) delegateExecution.getVariable("betaReader");
        User betaUser = this.userRepository.findById(Long.valueOf(betaUserCamunda.getId()))
                .orElseThrow(() -> new NotFoundException("User with given id doesn't exist"));

        Role betaRole = this.roleRepository.findByName("ROLE_BETA_READER");
        Role readerRole = this.roleRepository.findByName("ROLE_READER");

        betaUser.getRoles().remove(betaRole);
        betaUser.getRoles().add(readerRole);

        log.info("Saving user with reader role");
        this.userRepository.save(betaUser);
    }
}
