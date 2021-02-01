package upp.team5.literaryassociation.authorRegistration.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.common.dto.MembershipRequestDTO;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;
import upp.team5.literaryassociation.model.MembershipRequest;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(path = "/membership-requests")
public class MembershipRequestController {

    private final MembershipRequestService membershipRequestService;

    @Autowired
    public MembershipRequestController(MembershipRequestService membershipRequestService) {
        this.membershipRequestService = membershipRequestService;
    }

    @PreAuthorize("hasAuthority('ROLE_COMMITTEE_MEMBER')")
    @GetMapping
    public ResponseEntity<List<MembershipRequestDTO>> getRequests() {
        List<MembershipRequestDTO> requestDTOS = this.membershipRequestService.getAllRequests();
        return new ResponseEntity<>(requestDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_COMMITTEE_MEMBER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<MembershipRequestDTO> getRequest(@PathVariable Long id) {
        MembershipRequestDTO request = this.membershipRequestService.getRequest(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping(path = "/documents/{id}")
    public ResponseEntity<byte[]> getDocument(@PathVariable Long id) {
        return this.membershipRequestService.getDocument(id);
    }

    @PreAuthorize("hasAuthority('ROLE_PENDING_AUTHOR')")
    @GetMapping(path = "/author-request/{id}")
    public ResponseEntity<?> getAuthorMembershipRequest(@PathVariable String id) {
        MembershipRequest membershipRequest = this.membershipRequestService.getAuthorRequest(Long.parseLong(id));
        return  new ResponseEntity<>(membershipRequest.getId(), HttpStatus.OK);
    }
}
