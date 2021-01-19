package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;

import javax.validation.constraints.Email;

@Service
@Slf4j
public class NotifyChiefEditorAboutComplaintDelegate implements JavaDelegate{
    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long id = (Long) execution.getVariable("plagiarism-complaint-id");

        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.getPlagiarismComplaint(id);

        User chiefEditor = plagiarismComplaint.getComplainantBook().getChiefEditor();
        String to = chiefEditor.getEmail();
        String subject = "New plagiarism complaint";
        String body = "Hello,\n\nwe would like to inform you that book you have been editing has potentially been plagiated.\n\n ";
        body+="Book title: "+plagiarismComplaint.getComplainantBook().getTitle();
        emailService.Send(to, body, subject);

    }
}
