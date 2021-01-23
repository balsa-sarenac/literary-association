package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.common.service.NoteService;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.model.NoteType;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.HashMap;

@Service
@Slf4j
public class ProcessSuggestions implements JavaDelegate {
    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private PublishingRequestService publishingRequestService;

    @Autowired
    private NoteService noteService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));
        User editor = this.authUserService.getLoggedInUser();

        if(request.isPresent()){
            PublishingRequest publishingRequest = request.get();

            HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-more-suggestions");

            var suggestionsObj = formSubmission.get("suggestions");
            boolean suggestions = false;

            try {
                suggestions = Boolean.parseBoolean(suggestionsObj.toString());
            } catch (Exception e) {

            }

            delegateExecution.setVariable("suggestions", suggestions);
            if (suggestions) {
                publishingRequest.setStatus("Editor gave suggestions");

                Note note = new Note();
                note.setContent((String) formSubmission.get("textareaSuggestions"));
                note.setType(NoteType.SUGGESTION);
                note.setPublishingRequest(publishingRequest);
                note.setUser(editor);
                noteService.saveNote(note);
            }
            else {
                publishingRequest.setStatus("Book is sent to lector");
            }
            publishingRequestService.savePublishingRequest(publishingRequest);
        }
    }
}
