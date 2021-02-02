package upp.team5.literaryassociation.form.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.common.dto.FormFieldsDTO;
import upp.team5.literaryassociation.common.dto.FormSubmissionDTO;
import upp.team5.literaryassociation.common.dto.FormSubmissionFieldDTO;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.exception.BadInputException;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

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
    private AuthUserService authUserService;
    @Autowired
    private IdentityService identityService;

    public FormFieldsDTO getForm(String processInstanceId) {
        Task task = getTask(processInstanceId);

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        FormFieldsDTO formFieldsDTO = new FormFieldsDTO(processInstanceId, task.getId(), properties);

        return formFieldsDTO;
    }

    public void submitForm(String processInstanceId, FormSubmissionDTO formSubmissionDTO) {
        HashMap<String, Object> map = this.listToMap(formSubmissionDTO.getFormFields());

        Task task = getTask(processInstanceId);

        try {
            runtimeService.setVariable(processInstanceId, "data-" + task.getTaskDefinitionKey(), map);
        } catch(Exception e) {
            throw new BadInputException("Process instance no longer exists");
        }

        try {
            formService.submitTaskForm(task.getId(), map);
        } catch (Exception exception) {
            throw new BadInputException(exception.getMessage());
        }
    }

    private Task getTask(String processInstanceId) {
        User user = authUserService.getLoggedInUser();
        org.camunda.bpm.engine.identity.User camundaUser = null;
        Task task = null;

        if (user != null) {
            camundaUser = identityService.createUserQuery()
                    .userId(String.valueOf(user.getId())).singleResult();
        }
        if (camundaUser != null) {
            task = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskAssignee(camundaUser.getId())
                    .active()
                    .singleResult();
        }
        if (task == null) {
            task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        }
        return task;
    }

    private HashMap<String, Object> listToMap(List<FormSubmissionFieldDTO> formSubmissionDTOS) {
        HashMap<String, Object> map = new HashMap<>();
        for (FormSubmissionFieldDTO fs : formSubmissionDTOS) {
            map.put(fs.getId(), fs.getValue());
        }
        return map;
    }

}
