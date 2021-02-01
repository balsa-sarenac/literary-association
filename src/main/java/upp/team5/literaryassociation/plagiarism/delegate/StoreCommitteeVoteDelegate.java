package upp.team5.literaryassociation.plagiarism.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.authorRegistration.service.VoteService;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.model.Vote;
import upp.team5.literaryassociation.model.VoteOption;
import upp.team5.literaryassociation.plagiarism.service.PlagiarismComplaintService;

import java.util.HashMap;

@Service @Slf4j
public class StoreCommitteeVoteDelegate implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private VoteService voteService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Getting vote data");
        Long  requestId = (Long) delegateExecution.getVariable("plagiarism-complaint-id");
        PlagiarismComplaint complaint = plagiarismComplaintService.getPlagiarismComplaint(requestId);

        HashMap<String, Object> formSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-plagiarism-vote");
        boolean isPlagiarism;
        if (formSubmission.get("isPlagiarism") != null) {
            isPlagiarism = !formSubmission.get("isPlagiarism").equals("") && (boolean) formSubmission.get("isPlagiarism");
        } else {
            isPlagiarism = false;
        }

        log.info("Saving committee vote");
        Vote vote = new Vote();
        vote.setVoteTime(DateTime.now());
        vote.setVoteOption(isPlagiarism ? VoteOption.PLAGIARISM : VoteOption.NOT_PLAGIARISM);
        vote.setCommitteeMember(authUserService.getLoggedInUser());
        vote.setRound(complaint.getIteration());
        vote.setPlagiarismComplaint(complaint);
        voteService.saveVote(vote);
    }
}
