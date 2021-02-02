package upp.team5.literaryassociation.authorRegistration.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.authorRegistration.service.VoteService;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.common.service.NoteService;
import upp.team5.literaryassociation.model.*;

import java.util.Date;
import java.util.HashMap;

@Service @Slf4j
public class SaveVoteDelegate implements JavaDelegate {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private MembershipRequestService membershipRequestService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private NoteService noteService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        User committee = authUserService.getLoggedInUser();

        MembershipRequest membershipRequest = membershipRequestService.getMembershipRequest(
                (Long) delegateExecution.getVariable("membershipRequestId"));

        HashMap<String, Object> formFields = (HashMap<String, Object>) delegateExecution.getVariable("data-review-documents");

        log.info("Creating new vote");
        Vote vote = new Vote();

        String value = (String) formFields.get("accept-author");
        if ("approve".equals(value)) {
            vote.setVoteOption(VoteOption.APPROVE);
        } else if ("refuse".equals(value)) {
            vote.setVoteOption(VoteOption.REFUSE);

        } else if ("request_more".equals(value)) {
            vote.setVoteOption(VoteOption.REQUEST_MORE_MATERIAL);
        }

        vote.setMembershipRequest(membershipRequest);
        vote.setVoteTime(DateTime.now());
        vote.setRound(membershipRequest.getVoteRound());
        vote.setCommitteeMember(committee);

        voteService.saveVote(vote);

        if (!String.valueOf(formFields.get("textarea")).isBlank() && formFields.get("textarea") != null) {
            Note note = new Note();
            note.setContent((String) formFields.get("textarea"));
            note.setType(NoteType.COMMENT);
            note.setMembershipRequest(membershipRequest);
            note.setUser(committee);
            note.setDateTime(DateTime.now());
            noteService.saveNote(note);
        }
        delegateExecution.removeVariable("textarea");
        delegateExecution.removeVariable("accept-author");
    }
}
