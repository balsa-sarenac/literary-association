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
import upp.team5.literaryassociation.publishing.service.BookService;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;
import upp.team5.literaryassociation.register.service.GenreService;

import java.util.HashMap;

@Service
@Slf4j
public class ProcessInitialApproval implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));

        if(request.isPresent()){
            PublishingRequest publishingRequest = request.get();
            HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-read-manuscript");
            Boolean read = (Boolean)formSubmission.get("read");

            publishingRequest.setReviewed(read);
            if(read)
                publishingRequest.setStatus("Book upload requested");
            else {
                publishingRequest.setStatus("Reading rejected");
                delegateExecution.setVariable("explanation", formSubmission.get("explanation").toString());
            }

            publishingRequestService.savePublishingRequest(publishingRequest);
            delegateExecution.setVariable("readApproved", read);
        }
    }
}
