package upp.team5.literaryassociation.camunda.eventListener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.FormFieldImpl;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.camunda.type.MultiselectFormFieldType;
import upp.team5.literaryassociation.common.dto.FileDTO;
import upp.team5.literaryassociation.common.dto.UserDTO;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OnCreatePublishingTask implements TaskListener {
    @Autowired
    private FormService formService;

    @Autowired
    private PublishingRequestService publishingRequestService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void notify(DelegateTask delegateTask) {
        var requestId = delegateTask.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));

        if(request.isPresent()){
            PublishingRequest publishingRequest = request.get();

            TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
            List<FormField> fields = taskFormData.getFormFields();

            for(FormField field : fields){
                switch (field.getId()) {
                    case "title": {
                        var value = (String) field.getValue().getValue();
                        value = publishingRequest.getBook().getTitle();
                        break;
                    }
                    case "synopsis": {
                        var value = (String) field.getValue().getValue();
                        value = publishingRequest.getBook().getSynopsis();
                        break;
                    }
                    case "author": {
                        var value = (String) field.getValue().getValue();
                        var authors = publishingRequest.getBook().getAuthors();
                        if (authors != null) {
                            var author = (User) authors.toArray()[0];
                            delegateTask.setVariable("author", author.getFirstName());
                        }
                        break;
                    }
                    case "status": {
                        delegateTask.setVariable("status", publishingRequest.getStatus());
                        break;
                    }
                    case "sources": {
                        Map<String, String> enumType = ((EnumFormType) field.getType()).getValues();
                        enumType.clear();

                        var files = publishingRequestService.getFiles(Long.parseLong(requestId.toString()));
                        enumType.putAll(listToMap(files));
                        break;
                    }
                    case "book": {
                        Map<String, String> enumType = ((EnumFormType) field.getType()).getValues();
                        enumType.clear();
                        var files = publishingRequestService.getBookFile(Long.parseLong(requestId.toString()));
                        enumType.putAll(listToMap(files));
                        break;
                    }
                    case "betaReaders": {
                        List<UserDTO> readers = publishingRequestService.getAllBetaReadersForRequest(delegateTask.getVariable("publishing-request-id").toString());
                        MultiselectFormFieldType multiselectType = (MultiselectFormFieldType) field.getType();
                        multiselectType.setValues(listToMapUsers(readers));
                        break;
                    }
                    case "textareaSuggestions": {
                        delegateTask.setVariable("textareaSuggestions","");
                        break;
                    }
                }
            }
        }
    }

    private Map<String, String> listToMap(List<FileDTO> links) {
        Map<String, String> map = new HashMap<>();
        for (FileDTO f: links) {
            map.put(f.getUrl(), f.getName());
        }
        return map;
    }

    private Map<String, String> listToMapUsers(List<UserDTO> users) {
        Map<String, String> map = new HashMap<>();
        for (UserDTO u : users) {
            map.put(u.getId().toString(), u.getFirstName() + ' ' + u.getLastName() + ", " + u.getCity());
        }
        return map;
    }
}
