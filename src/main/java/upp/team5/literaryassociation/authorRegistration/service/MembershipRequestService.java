package upp.team5.literaryassociation.authorRegistration.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.repository.MembershipRequestRepository;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.HashSet;

@Service
@Slf4j
public class MembershipRequestService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private MembershipRequestRepository membershipRequestRepository;

    @Autowired
    private UserRepository userRepository;


    public void addNewRequest(HashSet<FileDB> files, String processId) {
        String userId = ((User)runtimeService.getVariable(processId,"loggedUser")).getId();
        MembershipRequest membershipRequest = new MembershipRequest();
        membershipRequest.setAuthor(userRepository.getUserByEmail(userId));
        membershipRequest.setDocuments(files);
        membershipRequestRepository.save(membershipRequest);
    }
}
