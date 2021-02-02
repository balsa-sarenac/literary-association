package upp.team5.literaryassociation.publishing.betaReader.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

@Service
@Slf4j
public class SendCommentsToAuthor implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long requestId = (Long) delegateExecution.getVariable("publishing-request-id");

        PublishingRequest publishingRequest = publishingRequestService.getPublishingRequest(requestId);

        publishingRequest.setStatus("Change book based on comments");
        publishingRequestService.savePublishingRequest(publishingRequest);
    }
}
