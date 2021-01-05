package upp.team5.literaryassociation.authorRegistration.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.authorRegistration.repository.VoteRepository;
import upp.team5.literaryassociation.common.dto.VoteDTO;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.model.Vote;
import upp.team5.literaryassociation.model.VoteOption;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
public class VoteService implements JavaDelegate {

    private final VoteRepository voteRepository;
    private final MembershipRequestService membershipRequestService;
    private final UserRepository userRepository;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    public VoteService(VoteRepository voteRepository, MembershipRequestService membershipRequestService, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.membershipRequestService = membershipRequestService;
        this.userRepository = userRepository;
    }

    public void newVote(VoteDTO voteDTO) {
        User committee = getLoggedInUser();

        MembershipRequest membershipRequest = this.membershipRequestService.getMembershipRequest(voteDTO.getRequestId());

        log.info("Creating new vote");
        Vote vote = new Vote();

        switch (voteDTO.getOption()) {
            case "approve" -> {
                vote.setVoteOption(VoteOption.APPROVE);
            }
            case "refuse" -> {
                vote.setVoteOption(VoteOption.REFUSE);
            }
            case "request_more" -> {
                vote.setVoteOption(VoteOption.REQUEST_MORE_MATERIAL);
            }
        }

        vote.setMembershipRequest(membershipRequest);
        vote.setVoteTime(new Date());
        vote.setRound(membershipRequest.getVoteRound());
        vote.setCommitteeMember(committee);

        this.voteRepository.save(vote);

        log.info("Getting process instance");
        ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("author-reg")
                .variableValueEquals("membershipRequestId", membershipRequest.getId())
                .singleResult();

        org.camunda.bpm.engine.identity.User camundaUser = identityService.createUserQuery().userId(String.valueOf(committee.getId())).singleResult();
        Task task = this.taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee(camundaUser.getId()).singleResult();
        taskService.claim(task.getId(), camundaUser.getId());

        log.info("Completing task");
        taskService.complete(task.getId());

    }

    private User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if(principal instanceof UserDetails){
             email = ((UserDetails)principal).getUsername();
        }
        else{
             email = principal.toString();
        }

        return this.userRepository.findByEmail(email);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long approvedVotes = 0;
        long refusedVotes = 0;
        long requestMoreVotes = 0;

        List<org.camunda.bpm.engine.identity.User> assigneeList = (List<org.camunda.bpm.engine.identity.User>) delegateExecution.getVariable("assigneeList");
        MembershipRequest membershipRequest = this.membershipRequestService.getMembershipRequest((Long) delegateExecution.getVariable("membershipRequestId"));

        List<Vote> votes = membershipRequest.getVotes().stream().filter(vote -> vote.getRound() == membershipRequest.getVoteRound()).collect(Collectors.toList());

        for (Vote vote : votes) {
            if (vote.getVoteOption().equals(VoteOption.APPROVE)) {
                approvedVotes++;
            } else if (vote.getVoteOption().equals(VoteOption.REFUSE)) {
                refusedVotes++;
            } else {
                requestMoreVotes++;
            }
        }

        if (requestMoreVotes != 0) {
            if (membershipRequest.getVoteRound() > 2) {
                delegateExecution.setVariable("decision", "rejected");
            } else {
                delegateExecution.setVariable("decision", "moreDocuments");
            }
        } else if (refusedVotes > assigneeList.size() / 2) {
            delegateExecution.setVariable("decision", "rejected");
        } else if (approvedVotes >= assigneeList.size() / 2) {
            delegateExecution.setVariable("decision", "approved");
        }

    }
}
