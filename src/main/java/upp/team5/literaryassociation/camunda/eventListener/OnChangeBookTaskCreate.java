package upp.team5.literaryassociation.camunda.eventListener;

import camundajar.impl.scala.Array;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Genre;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.model.NoteType;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
       // boolean exists = notes.stream().anyMatch(x->x.getType().equals(NoteType.COMMENT));
        ArrayList<Note> notesArray = new ArrayList<Note>(notes);
        boolean exists = notesArray.get(notesArray.size()-1).getType().equals(NoteType.COMMENT);
        //if(exists) {
            for (FormField field : properties) {
                if (field.getId().equals("reupload") && exists) {
                    Map<String, String> props = field.getProperties();
//                    if (props.containsKey("oneIfNeeded")) {
//                        props.replace("oneIfNeeded", "false");
//                    }
                        field.getProperties().put("oneIfNeeded", "false");
                }
                else if(field.getId().equals("reupload") && !exists){
                    field.getProperties().put("oneIfNeeded", "true");

                }
            }
    }
}
