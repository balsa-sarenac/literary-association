package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.common.service.NoteService;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.model.NoteType;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;

import java.util.HashMap;

@Service @Slf4j
public class StoreEditorsNotes implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private AuthUserService authUserService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Getting data from process variable");
        Long  requestId = (Long) delegateExecution.getVariable("plagiarism-complaint-id");
        PlagiarismComplaint complaint = plagiarismComplaintService.getPlagiarismComplaint(requestId);

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-editor-notes");

        String noteContent = (String) formSubmission.get("textarea");

        log.info("Saving notes editor left");
        Note note = new Note();
        note.setType(NoteType.COMMENT);
        note.setContent(noteContent);
        note.setUser(authUserService.getLoggedInUser());
        note.setPlagiarismComplaint(complaint);
        note.setDateTime(DateTime.now());
        noteService.saveNote(note);

        log.info("Set send to committee variable to true");
        delegateExecution.setVariable("sentToCommittee", true);
    }
}
