package upp.team5.literaryassociation.camunda.eventListener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.camunda.type.MultiselectFormFieldType;
import upp.team5.literaryassociation.common.dto.UserDTO;
import upp.team5.literaryassociation.model.Genre;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OnTaskCreateChooseReaders implements TaskListener {
    @Autowired
    private FormService formService;

    private PublishingRequestService publishingRequestService;


    public OnTaskCreateChooseReaders(PublishingRequestService publishingRequestService){
        this.publishingRequestService = publishingRequestService;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
        List<FormField> properties = taskFormData.getFormFields();

        List<UserDTO> readers = publishingRequestService.getAllBetaReadersForRequest(delegateTask.getVariable("publishing-request-id").toString());

        for(FormField field : properties){
            if(field.getId().equals("betaReaders")){
                MultiselectFormFieldType multiselectType = (MultiselectFormFieldType) field.getType();
                multiselectType.setValues(listToMap(readers));
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
