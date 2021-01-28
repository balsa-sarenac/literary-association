package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.PlagiarismComplaintStage;
import upp.team5.literaryassociation.model.Vote;
import upp.team5.literaryassociation.model.VoteOption;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
public class ProcessCommitteeVotesDelegate implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Getting plagiarism complaint data");
        Long  requestId = (Long) delegateExecution.getVariable("plagiarism-complaint-id");
        PlagiarismComplaint complaint = plagiarismComplaintService.getPlagiarismComplaint(requestId);
        List<User> committeeList = (List<User>) delegateExecution.getVariable("committeeList");

        List<Vote> committeeVotes = complaint.getVotes().stream()
                .filter(vote -> vote.getRound() == complaint.getIteration())
                .collect(Collectors.toList());

        int plagiarismVotes = 0;
        int notPlagiarismVotes = 0;
        for (Vote committeeVote : committeeVotes) {
            switch (committeeVote.getVoteOption()) {
                case PLAGIARISM -> plagiarismVotes++;
                case NOT_PLAGIARISM -> notPlagiarismVotes++;
            }
        }

        if ((plagiarismVotes == 0 && plagiarismVotes == committeeList.size()) ||
                (notPlagiarismVotes == 0 && notPlagiarismVotes == committeeList.size())) {
            delegateExecution.setVariable("unanimous", true);
            complaint.setPlagiarismComplaintStage(PlagiarismComplaintStage.FINISH);
        } else {
            delegateExecution.setVariable("unanimous", false);
            delegateExecution.setVariable("minEditors", 0);
            delegateExecution.setVariable("maxEditors", 0);

            complaint.setPlagiarismComplaintStage(PlagiarismComplaintStage.CHOOSE_EDITORS);
            complaint.setIteration(complaint.getIteration() + 1);
            complaint.setIterationStart(DateTime.now());
            complaint.getEditorsOnInvestigation()
                    .forEach(editor -> editor.getComplaintsToInvestigate().remove(complaint));
            complaint.setEditorsOnInvestigation(new HashSet<>());
        }
        userRepository.saveAll(complaint.getEditorsOnInvestigation());
        plagiarismComplaintService.save(complaint);
    }
}
