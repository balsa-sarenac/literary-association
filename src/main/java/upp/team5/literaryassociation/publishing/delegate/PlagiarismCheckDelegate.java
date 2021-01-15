package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import javax.ws.rs.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service @Slf4j
public class PlagiarismCheckDelegate implements JavaDelegate {

    @Autowired
    private PublishingRequestService publishingRequestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Finding publishing request");
        String correlationId = (String) delegateExecution.getVariable("correlationId");
        Long publishingRequestId = (Long) delegateExecution.getVariable("publishing-request-id");
        PublishingRequest publishingRequest = this.publishingRequestService.getById(publishingRequestId).orElseThrow(() -> {
            delegateExecution.getProcessEngineServices()
                    .getRuntimeService()
                    .createMessageCorrelation("ErrorEvent")
                    .processInstanceBusinessKey(correlationId)
                    .correlateWithResult();
            throw new NotFoundException("Publishing request with given id doesn't exist.");
        });

        log.info("Loading dummy files");
        loadFilesAndAddThemToRequest(publishingRequest);
        log.info("Saving request with new files");
        publishingRequestService.savePublishingRequest(publishingRequest);

        delegateExecution.getProcessEngineServices()
                .getRuntimeService()
                .createMessageCorrelation("PlagiarismCheckDone")
                .processInstanceBusinessKey(correlationId)
                .correlateWithResult();
    }

    private void loadFilesAndAddThemToRequest(PublishingRequest publishingRequest) throws IOException {
        byte[] mockFileData = Files.readAllBytes(Paths.get("src/main/resources/plagiarismFiles/mocked-file"));
        byte[] alsoMockFileData = Files.readAllBytes(Paths.get("src/main/resources/plagiarismFiles/also-mocked-file"));
        FileDB fileDBFirst = new FileDB();
        fileDBFirst.setName("mock-file");
        fileDBFirst.setData(mockFileData);
        fileDBFirst.setType("txt");
        FileDB fileDBSecond = new FileDB();
        fileDBSecond.setName("also-mock-file");
        fileDBSecond.setData(alsoMockFileData);
        fileDBSecond.setType("txt");

        publishingRequest.getPotentialPlagiarismSet().add(fileDBFirst);
        publishingRequest.getPotentialPlagiarismSet().add(fileDBSecond);
    }
}
