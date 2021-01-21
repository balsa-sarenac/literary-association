package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import javax.ws.rs.NotFoundException;
import java.util.HashMap;

@Service
@Slf4j
public class ProcessIfOriginal implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    private final AuthUserService authUserService;

    @Autowired
    public ProcessIfOriginal(AuthUserService authUserService){
        this.authUserService = authUserService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));

        if(request.isPresent()){
            PublishingRequest publishingRequest = request.get();

            HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-plagiarism");
            Boolean original = (Boolean)formSubmission.get("original");

            User loggedUser = authUserService.getLoggedInUser();
            org.camunda.bpm.engine.identity.User camundaUser = identityService.createUserQuery().userId(String.valueOf(loggedUser.getId())).singleResult();
            Task task = this.taskService.createTaskQuery().processInstanceId(delegateExecution.getProcessInstanceId()).active().singleResult();

            var u = task.getAssignee();

            if(u.equals(camundaUser.getId())) {
                delegateExecution.setVariable("original", original);
                if (original)
                    publishingRequest.setStatus("Book is original");
                else
                    publishingRequest.setStatus("Book is not original");

                publishingRequestService.savePublishingRequest(publishingRequest);

                log.info("Completing task");
                taskService.complete(task.getId());
            }
        }
    }
}
