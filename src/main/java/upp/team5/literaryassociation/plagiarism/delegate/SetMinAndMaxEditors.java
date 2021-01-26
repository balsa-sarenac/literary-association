package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.NoteService;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service @Slf4j
public class SetMinAndMaxEditors implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;
    @Autowired
    private NoteService noteService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

    }
}
