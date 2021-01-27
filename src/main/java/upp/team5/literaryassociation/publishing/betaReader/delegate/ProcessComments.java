package upp.team5.literaryassociation.publishing.betaReader.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.dto.NoteDTO;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.common.service.NoteService;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.model.NoteType;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.HashMap;

@Service @Slf4j
public class ProcessComments implements JavaDelegate {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private PublishingRequestService publishingRequestService;

    @Autowired
    private NoteService noteService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        HashMap<String, Object> formFields = (HashMap<String, Object>) delegateExecution.getVariable("data-beta-comments");
        Long requestId = (Long) delegateExecution.getVariable("publishing-request-id");

        User betaReader = this.authUserService.getLoggedInUser();
        PublishingRequest publishingRequest = publishingRequestService.getPublishingRequest(requestId);

        Note note = new Note();
        note.setContent((String) formFields.get("textarea"));
        note.setType(NoteType.COMMENT);
        note.setPublishingRequest(publishingRequest);
        note.setUser(betaReader);
        note.setDateTime(DateTime.now());
        note.setDeleted(false);
        noteService.saveNote(note);

        publishingRequest.setStatus("Change book based on comments");
        publishingRequestService.savePublishingRequest(publishingRequest);

    }

}
