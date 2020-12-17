package upp.team5.literaryassociation.form.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.register.dto.FormFieldsDTO;

import java.util.List;

@Service
@Slf4j
public class GenericFormService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;

    public FormFieldsDTO getForm(String processInstanceId) {

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        FormFieldsDTO formFieldsDTO = new FormFieldsDTO(processInstanceId, task.getId(), properties);

        return formFieldsDTO;
    }
}
