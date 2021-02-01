package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.EmailService;

import java.util.List;

@Service @Slf4j
public class NotifyEditorsAboutInvestigationStart implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Notifying editors about investigation");

        List<User> editors = (List<User>) delegateExecution.getVariable("editorsList");

        for (User editor : editors) {
            emailService.Send(editor.getEmail(), "Chief editor has assigned you to investigate a plagiarism complaint.",
                    "New plagiarism complaint to investigate");
        }
    }
}
