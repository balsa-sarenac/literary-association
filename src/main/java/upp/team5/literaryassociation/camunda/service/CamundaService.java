package upp.team5.literaryassociation.camunda.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.dto.ProcessDTO;

@Service @Slf4j
public class CamundaService {

    @Autowired
    private RuntimeService runtimeService;

    public ProcessDTO getProcessInstanceId(String requestType, Long publishingRequestId) {
        log.info("Getting proces instance for " + requestType + ": " + publishingRequestId);

        String type;
        String definitionKey;
        if (requestType.equals("membershipRequestId")) {
            type = requestType;
            definitionKey = "author-reg";
        }
        else if (requestType.equals("publishingRequestId")) {
            type = "publishing-request-id";
            definitionKey = "book-publishing";
        }
        else {
            type = "plagiarism-complaint-id";
            definitionKey = "plagiarism-process";
        }

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(definitionKey)
                .variableValueEquals(type, publishingRequestId)
                .singleResult();

        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setProcessId(processInstance.getId());
        return processDTO;
    }
}
