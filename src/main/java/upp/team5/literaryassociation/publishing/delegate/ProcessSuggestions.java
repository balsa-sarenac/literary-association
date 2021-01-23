package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.HashMap;

@Service
@Slf4j
public class ProcessSuggestions implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));

        if(request.isPresent()){
            PublishingRequest publishingRequest = request.get();

            HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-more-suggestions");

            

            publishingRequestService.savePublishingRequest(publishingRequest);
        }
    }
}
