package upp.team5.literaryassociation.authorRegistration.service;

import lombok.extern.slf4j.Slf4j;
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

@Service
@Slf4j
public class ProcessApplicationService implements JavaDelegate {

//    @Autowired
//    private MembershipRequestService membershipRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Process Application started");

//        HashSet<FileDB> files = (HashSet<FileDB>)execution.getVariable("files");

        List<String> roleName = new ArrayList<String>();
        roleName.add("ROLE_COMMITTEE_MEMBER");
        roleName.add("ROLE_HEAD_OF_COMMITTEE");
        List<Role> roles = roleRepository.findAllByNameIn(roleName);
        List<User> committee = userRepository.findAllByRolesIn(roles);

        execution.setVariable("assigneeList", committee);

    }
}
