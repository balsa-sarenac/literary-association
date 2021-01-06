package upp.team5.literaryassociation.authorMembershipPayment.service;

import org.camunda.bpm.engine.FormService;
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
    private FormService formService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private UserRepository userRepository;

    public void payFee(String processId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(getLoggedInUser().getId().toString(), runtimeService.getVariable(processId, "membershipRequestId") );

        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();

        formService.submitTaskForm(task.getId(), map);
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
