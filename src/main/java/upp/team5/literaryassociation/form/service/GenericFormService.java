package upp.team5.literaryassociation.form.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.common.dto.ProcessDTO;
import upp.team5.literaryassociation.form.dto.FormFieldsDTO;
import upp.team5.literaryassociation.form.dto.FormSubmissionDTO;
import upp.team5.literaryassociation.form.dto.FormSubmissionFieldDTO;
import upp.team5.literaryassociation.model.MembershipRequest;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class GenericFormService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private MembershipRequestService membershipRequestService;

    public FormFieldsDTO getForm(String processInstanceId) {

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        FormFieldsDTO formFieldsDTO = new FormFieldsDTO(processInstanceId, task.getId(), properties);

        return formFieldsDTO;
    }


    public void submitForm(String processInstanceId, FormSubmissionDTO formSubmissionDTO) {
        HashMap<String, Object> map = this.listToMap(formSubmissionDTO.getFormFields());

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        //String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "data-" + task.getTaskDefinitionKey(), map);

        formService.submitTaskForm(task.getId(), map);
    }

    private HashMap<String, Object> listToMap(List<FormSubmissionFieldDTO> formSubmissionDTOS) {
        HashMap<String, Object> map = new HashMap<>();
        for (FormSubmissionFieldDTO fs : formSubmissionDTOS) {
            map.put(fs.getId(), fs.getValue());
        }
        return map;
    }


    public String getProcessId(String userId) {
        Long membershipRequestId = membershipRequestService.getAuthorMembershipRequest(userId);

        ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(userId)
                .variableValueEquals("membershipRequestId", membershipRequestId)
                .singleResult();

        return pi.getId();
    }
}
