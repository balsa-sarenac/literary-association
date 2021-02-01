package upp.team5.literaryassociation.camunda.eventListener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.model.NoteType;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.*;

@Service
@Slf4j
public class OnChangeBookTaskCreate implements TaskListener {
    @Autowired
    private FormService formService;

    @Autowired
    private PublishingRequestService publishingRequestService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
        List<FormField> properties = taskFormData.getFormFields();

        Long publishingRequestId = (Long) delegateTask.getVariable("publishing-request-id");
        Set<Note> notes = publishingRequestService.getPublishingRequest(publishingRequestId).getNotes();
        ArrayList<Note> notesArray = new ArrayList<Note>(notes);
        if(notesArray.size()>0) {
            boolean exists = notesArray.get(notesArray.size() - 1).getType().equals(NoteType.COMMENT);
            for (FormField field : properties) {
                if (field.getId().equals("reupload") && exists) {
                    Map<String, String> props = field.getProperties();
                    field.getProperties().put("oneIfNeeded", "false");
                } else if (field.getId().equals("reupload") && !exists) {
                    field.getProperties().put("oneIfNeeded", "true");

                }
                else if(field.getId().equals("newComments")){
                    Map<String, String> enumType = ((EnumFormType) field.getType()).getValues();
                    enumType.clear();
                    enumType.putAll(setToMap(notes, false));
                }
                else if(field.getId().equals("oldComments")){
                    Map<String, String> enumType = ((EnumFormType) field.getType()).getValues();
                    enumType.clear();
                    enumType.putAll(setToMap(notes, true));
                }

            }
        }
    }

    private Map<String, String> setToMap(Set<Note> notes, boolean old) {
        Map<String, String> map = new HashMap<>();
        for (Note n : notes) {
            if(n.getDeleted() && old)
                map.put(n.getId().toString(), n.getType().toString()+ " - " + n.getUser().getFirstName() + " " + n.getUser().getLastName() + ": " + n.getContent());
            else if(!n.getDeleted() && !old)
                map.put(n.getId().toString(), n.getType().toString()+ " - " + n.getUser().getFirstName() + " " + n.getUser().getLastName() + ": " + n.getContent());
        }
        return map;
    }
}
