package upp.team5.literaryassociation.plagiarism.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.PlagiarismComplaintStage;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.BookService;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class ProcessComplaintService implements JavaDelegate {
    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        HashMap<String, Object> formFields = (HashMap<String, Object>) execution.getVariable("data-file-complaint");

        User complainant = authUserService.getLoggedInUser();
        Book complainantBook = bookService.getBook(Long.valueOf((Integer) formFields.get("authorBook")));
        Book plagiarismBook = bookService.getBook(Long.parseLong(String.valueOf(formFields.get("plagiarismBook"))));

        PlagiarismComplaint plagiarismComplaint = new PlagiarismComplaint();
        plagiarismComplaint.setPlagiarismComplaintStage(PlagiarismComplaintStage.CHOOSE_EDITORS);
        plagiarismComplaint.setIteration(0);
        plagiarismComplaint.setIterationStart(DateTime.now());
        plagiarismComplaint.setComplainant(complainant);
        plagiarismComplaint.setComplainantBook(complainantBook);
        plagiarismComplaint.setPlagiarism(plagiarismBook);
        plagiarismComplaint = plagiarismComplaintService.save(plagiarismComplaint);

        complainant.getComplaints().add(plagiarismComplaint);
        userRepository.save(complainant);

        complainantBook.getBeingPlagiated().add(plagiarismComplaint);
        bookService.saveBook(complainantBook);

        plagiarismBook.getAccusedOfPlagiarism().add(plagiarismComplaint);
        bookService.saveBook(plagiarismBook);

        runtimeService.setVariable(execution.getProcessInstanceId(), "plagiarism-complaint-id", plagiarismComplaint.getId());

        List<User> chiefEditors = userRepository.findByRoles_Id(7L);
        execution.setVariable("chiefEditor", String.valueOf(chiefEditors.get(0).getId()));
    }
}
