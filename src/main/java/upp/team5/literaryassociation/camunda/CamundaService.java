package upp.team5.literaryassociation.camunda;

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

    public ProcessDTO getProcessInstanceId(Long publishingRequestId) {
        log.info("Getting proces instance if for publishing request" + publishingRequestId);

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("book-publishing")
                .variableValueEquals("publishing-request-id", publishingRequestId)
                .singleResult();

        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setProcessId(processInstance.getId());
        return processDTO;
    }
}
