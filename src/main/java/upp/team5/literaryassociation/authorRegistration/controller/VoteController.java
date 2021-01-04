package upp.team5.literaryassociation.authorRegistration.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import upp.team5.literaryassociation.authorRegistration.service.VoteService;
import upp.team5.literaryassociation.common.dto.VoteDTO;

@CrossOrigin
@Controller @Slf4j
@RequestMapping(path = "vote")
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PreAuthorize("hasAuthority('ROLE_COMMITTEE_MEMBER')")
    @PostMapping
    public ResponseEntity<?> postVote(@RequestBody VoteDTO voteDTO) {
        this.voteService.newVote(voteDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
