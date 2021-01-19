package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Genre;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.*;

@Service
@Slf4j
public class SendToBetaReadersService implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdentityService identityService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));

        List<org.camunda.bpm.engine.identity.User> betaReadersList = new LinkedList<>();

        if(request.isPresent()) {
            HashMap<String, Object> readers = (HashMap<String, Object>) delegateExecution.getVariable("data-choose-beta");

            Set<User> users = new HashSet<>();
            var selected = (List<String>) readers.get("betaReaders");
            for (String u : selected) {
                User user = userRepository.findById(Long.parseLong(u)).orElseThrow();
                if (user != null) {
                    users.add(user);
                    user.getEarlyAccessBooks().add(request.get());
                    userRepository.save(user);
                }
            }

            for (User us : users) {
                var camUs = identityService.createUserQuery().userId(us.getId().toString()).singleResult();
                if(camUs != null)
                    betaReadersList.add(camUs);
            }

            delegateExecution.setVariable("betaReadersList", betaReadersList);
        }
    }
}
