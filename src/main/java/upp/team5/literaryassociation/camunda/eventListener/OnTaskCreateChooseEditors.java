package upp.team5.literaryassociation.camunda.eventListener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.camunda.type.MultiselectFormFieldType;
import upp.team5.literaryassociation.common.dto.UserDTO;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service @Slf4j
public class OnTaskCreateChooseEditors implements TaskListener {

    @Autowired
    private FormService formService;

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
        List<FormField> properties = taskFormData.getFormFields();

        Long complaintId = (Long) delegateTask.getVariable("plagiarism-complaint-id");
        List<UserDTO> editors = plagiarismComplaintService.getEditorsThatCanLeaveNotes(complaintId);

        String minEditors = null;
        String maxEditors = null;
        try {
            minEditors = (String) delegateTask.getVariable("minEditors");
            maxEditors = (String) delegateTask.getVariable("maxEditors");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (minEditors == null || minEditors.equals("")) {
            minEditors = "2";
        }

        if (maxEditors == null || maxEditors.equals("")) {
            maxEditors = "-1";
        }

        for (FormField formField : properties) {
            if (formField.getId().equals("editors")) {
                MultiselectFormFieldType multiselectFormFieldType = (MultiselectFormFieldType) formField.getType();
                multiselectFormFieldType.setValues(listToMap(editors));
                formField.getProperties().put("minEditors", minEditors);
                formField.getProperties().put("maxEditors", maxEditors);
                break;
            }
        }
    }

    private Map<String, String> listToMap(List<UserDTO> users) {
        Map<String, String> map = new HashMap<>();
        for (UserDTO u : users) {
            map.put(u.getId().toString(), u.getFirstName() + ' ' + u.getLastName() + ", " + u.getCity());
        }
        return map;
    }
}
