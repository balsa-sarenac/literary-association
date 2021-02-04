package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.PlagiarismComplaintStage;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetCommitteeListDelegate implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IdentityService identityService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Getting plagiarism complaint id");
        Long  requestId = (Long) delegateExecution.getVariable("plagiarism-complaint-id");
        PlagiarismComplaint complaint = plagiarismComplaintService.getPlagiarismComplaint(requestId);

        complaint.setPlagiarismComplaintStage(PlagiarismComplaintStage.COMMITTEE_VOTING);
        plagiarismComplaintService.save(complaint);

        Role committee = roleRepository.findByName("ROLE_COMMITTEE_MEMBER");
        List<User> users = userRepository.findAllByEnabledAndRolesIn(true, List.of(committee));

        log.info("Creating list of camunda users");
        List<org.camunda.bpm.engine.identity.User> camundaUsers = new ArrayList<>();
        for (User user: users) {
            camundaUsers.add(identityService.createUserQuery().userId(String.valueOf(user.getId())).singleResult());
        }
        delegateExecution.setVariable("committeeList", camundaUsers);
    }
}
