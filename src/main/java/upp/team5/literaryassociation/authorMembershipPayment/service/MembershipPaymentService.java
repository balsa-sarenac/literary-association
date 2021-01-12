package upp.team5.literaryassociation.authorMembershipPayment.service;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.HashMap;

@Service
public class MembershipPaymentService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserRepository userRepository;

    public void payFee(String processId) {
        //HashMap<String, Object> map = new HashMap<>();
        //map.put(getLoggedInUser().getId().toString(), runtimeService.getVariable(processId, "membershipRequestId") );

        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();

        //formService.submitTaskForm(task.getId(), map);//complete task
        User dbUser = getLoggedInUser();
        var cUser = identityService.createUserQuery().userEmail(dbUser.getEmail()).singleResult();
        taskService.claim(task.getId(), cUser.getId());
        taskService.complete(task.getId());
    }

    private User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if(principal instanceof UserDetails){
            email = ((UserDetails)principal).getUsername();
        }
        else{
            email = principal.toString();
        }

        return this.userRepository.findByEmail(email);
    }
}
