package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;
import upp.team5.literaryassociation.security.service.RoleService;

import java.util.HashSet;

@Service
@Slf4j
public class SendBookToLector implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RoleService roleService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));

        if(request.isPresent()){
            PublishingRequest publishingRequest = request.get();
            Role role = roleService.getByName("ROLE_LECTOR");
            User lector = customUserDetailsService.getUsersByRole(role.getId()).get(0);

            var books = lector.getLectorBooks();
            if(books == null) {
                books = new HashSet<>();
            }
            books.add(publishingRequest.getBook());
            lector.setLectorBooks(books);
            customUserDetailsService.saveUser(lector);
            delegateExecution.setVariable("lector" ,lector.getId().toString());
        }

    }
}
