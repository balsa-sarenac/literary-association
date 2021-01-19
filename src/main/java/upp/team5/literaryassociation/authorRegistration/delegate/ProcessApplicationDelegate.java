package upp.team5.literaryassociation.authorRegistration.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessApplicationDelegate implements JavaDelegate {

//    @Autowired
//    private MembershipRequestService membershipRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private IdentityService identityService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Process Application started");

//        HashSet<FileDB> files = (HashSet<FileDB>)execution.getVariable("files");

        List<String> roleName = new ArrayList<String>();
        roleName.add("ROLE_COMMITTEE_MEMBER");
        roleName.add("ROLE_HEAD_OF_COMMITTEE");
        List<Role> roles = roleRepository.findAllByNameIn(roleName);
        List<User> committee = userRepository.findAllByRolesIn(roles);
        List<String> ids = committee.stream().map(user -> String.valueOf(user.getId())).collect(Collectors.toList());

        List<org.camunda.bpm.engine.identity.User> camundaCommittee = identityService.createUserQuery().list();

        camundaCommittee.removeIf(user -> !ids.contains(user.getId()));

        execution.setVariable("assigneeList", camundaCommittee);

    }
}
