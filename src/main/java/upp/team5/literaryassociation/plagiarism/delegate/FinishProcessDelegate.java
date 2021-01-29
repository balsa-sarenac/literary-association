package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;
import upp.team5.literaryassociation.publishing.service.BookService;

@Service @Slf4j
public class FinishProcessDelegate implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;
    @Autowired
    private BookService bookService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Saving committee members decision");
        Long  requestId = (Long) delegateExecution.getVariable("plagiarism-complaint-id");
        PlagiarismComplaint complaint = plagiarismComplaintService.getPlagiarismComplaint(requestId);

        if ((boolean) delegateExecution.getVariable("isPlagiarism")) {
            Book book = complaint.getPlagiarism();
            book.setPlagiarism(true);
            bookService.saveBook(book);
        }

    }
}
