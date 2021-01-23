package upp.team5.literaryassociation.plagiarism.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.plagiarism.repository.PlagiarismComplaintRepository;
import upp.team5.literaryassociation.publishing.service.BookService;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import java.util.HashMap;

@Service
@Slf4j
public class ProcessComplaintService implements JavaDelegate {
    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private CustomUserDetailsService userService;

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
        Book complainantBook = bookService.getBook(Long.parseLong((String)formFields.get("authorBook")));
        Book plagiarismBook = bookService.getBook(Long.parseLong((String)formFields.get("plagiarismBook")));

        PlagiarismComplaint plagiarismComplaint = new PlagiarismComplaint();
        plagiarismComplaint.setComplainant(complainant);
        plagiarismComplaint.setComplainantBook(complainantBook);
        plagiarismComplaint.setPlagiarism(plagiarismBook);
        plagiarismComplaint = plagiarismComplaintService.save(plagiarismComplaint);

        complainant.getComplaints().add(plagiarismComplaint);
        userService.saveUser(complainant);

        complainantBook.getBeingPlagiated().add(plagiarismComplaint);
        bookService.saveBook(complainantBook);

        plagiarismBook.getAccusedOfPlagiarism().add(plagiarismComplaint);
        bookService.saveBook(plagiarismBook);

        runtimeService.setVariable(execution.getProcessInstanceId(), "plagiarism-complaint-id", plagiarismComplaint.getId());

    }
}