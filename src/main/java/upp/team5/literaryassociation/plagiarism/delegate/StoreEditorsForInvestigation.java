package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.camunda.type.MultiselectFormFieldType;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.PlagiarismComplaintStage;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service @Slf4j
public class StoreEditorsForInvestigation implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdentityService identityService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Saving editors for plagiarism investigation");
        Long  requestId = (Long) delegateExecution.getVariable("plagiarism-complaint-id");
        PlagiarismComplaint complaint = plagiarismComplaintService.getPlagiarismComplaint(requestId);

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-choose-editors");

        List<String> selected = (List<String>) formSubmission.get("editors");

        List<Long> ids = selected.stream().map(Long::parseLong).collect(Collectors.toList());
        List<User> editors = userRepository.findAllById(ids);

        for (User editor : editors) {
            editor.getComplaintsToInvestigate().add(complaint);
        }
        complaint.getEditorsOnInvestigation().addAll(editors);
        complaint.setPlagiarismComplaintStage(PlagiarismComplaintStage.EDITORS_LEAVE_NOTES);
        userRepository.saveAll(editors);
        plagiarismComplaintService.save(complaint);

        log.info("Creating camunda process variable");
        List<org.camunda.bpm.engine.identity.User> camundaEditors = new ArrayList<>();
        for (String userId : selected) {
            var camUs = identityService.createUserQuery().userId(userId).singleResult();
            if(camUs != null)
                camundaEditors.add(camUs);
        }

        delegateExecution.setVariable("editorsList", camundaEditors);

    }
}
