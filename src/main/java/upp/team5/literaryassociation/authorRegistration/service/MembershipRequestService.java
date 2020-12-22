package upp.team5.literaryassociation.authorRegistration.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private IdentityService identityService;

    @Autowired
    private MembershipRequestRepository membershipRequestRepository;

    @Autowired
    private UserRepository userRepository;


    public void addNewRequest(HashSet<FileDB> files, String processId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if(principal instanceof UserDetails){
             username = ((UserDetails)principal).getUsername();
        }
        else{
             username = principal.toString();
        }

        User user = identityService.createUserQuery().userEmail(username).singleResult();
        MembershipRequest membershipRequest = new MembershipRequest();
        membershipRequest.setAuthor(userRepository.getUserByEmail(user.getEmail()));
        membershipRequest.setDocuments(files);
        membershipRequest = membershipRequestRepository.save(membershipRequest);
    }
}
