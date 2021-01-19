package upp.team5.literaryassociation.plagiarism.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.dto.PlagiarismComplaintDTO;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.plagiarism.repository.PlagiarismComplaintRepository;
import upp.team5.literaryassociation.publishing.service.BookService;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import javax.ws.rs.NotFoundException;

@Service
@Slf4j
public class PlagiarismComplaintService {
    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private PlagiarismComplaintRepository plagiarismComplaintRepository;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    public void processComplaint(PlagiarismComplaintDTO plagiarismComplaintDTO, Long authorId, String processId) {
        User complainant = userService.getUserById(authorId);
        Book complainantBook = bookService.getBook(plagiarismComplaintDTO.getPlagiated().getId());
        Book plagiarismBook = bookService.getBook(plagiarismComplaintDTO.getPlagiarism().getId());

        PlagiarismComplaint plagiarismComplaint = new PlagiarismComplaint();
        plagiarismComplaint.setComplainant(complainant);
        plagiarismComplaint.setComplainantBook(complainantBook);
        plagiarismComplaint.setPlagiarism(plagiarismBook);
        plagiarismComplaint = plagiarismComplaintRepository.save(plagiarismComplaint);

        complainant.getComplaints().add(plagiarismComplaint);
        userService.saveUser(complainant);

        complainantBook.getBeingPlagiated().add(plagiarismComplaint);
        bookService.saveBook(complainantBook);

        plagiarismBook.getAccusedOfPlagiarism().add(plagiarismComplaint);
        bookService.saveBook(plagiarismBook);

        runtimeService.setVariable(processId, "plagiarism-complaint-id", plagiarismComplaint.getId());

        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskService.complete(task.getId());

    }

    public PlagiarismComplaint getPlagiarismComplaint(Long id){
        return plagiarismComplaintRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plagiarism complaint with given id doesn't exist"));
    }
}
