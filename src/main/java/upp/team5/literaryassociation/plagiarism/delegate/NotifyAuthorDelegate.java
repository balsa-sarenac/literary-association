package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;

@Service
@Slf4j
public class NotifyAuthorDelegate implements JavaDelegate {
    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = (Long) delegateExecution.getVariable("plagiarism-complaint-id");

        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.getPlagiarismComplaint(id);
        String body = "";
        if ((boolean) delegateExecution.getVariable("plagiarism")) {
            body = "Hello,\n\nyour plagiarism complaint has been accepted.\n\nAccused book has  been marked as plagiarism.";
        }
        else {
            body = "Hello,\n\nplagiarism complaint has been refused.";
        }

        User author = plagiarismComplaint.getComplainantBook().getAuthors().iterator().next();
        String to = author.getEmail();
        String subject = "Plagiarism complaint is processed";
        emailService.Send(to, body, subject);
    }
}
