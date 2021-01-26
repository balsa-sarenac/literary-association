package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.PlagiarismComplaintStage;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service @Slf4j
public class SetMinAndMaxEditors implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Getting data from process variable");
        Long  requestId = (Long) delegateExecution.getVariable("plagiarism-complaint-id");
        PlagiarismComplaint complaint = plagiarismComplaintService.getPlagiarismComplaint(requestId);

        Set<User> editors = complaint.getEditorsOnInvestigation();
        List<User> editorsThatHaveLeftANote = complaint.getNotes().stream()
                .filter(note -> note.getDateTime().isAfter(complaint.getIterationStart()))
                .map(Note::getUser).collect(Collectors.toList());
        editors.removeIf(editorsThatHaveLeftANote::contains);

        delegateExecution.setVariable("minEditors", editors.size());
        delegateExecution.setVariable("maxEditors", editors.size());

//        complaint.setIteration(complaint.getIteration() + 1);
//        complaint.setIterationStart(DateTime.now());
        complaint.setPlagiarismComplaintStage(PlagiarismComplaintStage.CHOOSE_EDITORS);
        plagiarismComplaintService.save(complaint);
    }
}
