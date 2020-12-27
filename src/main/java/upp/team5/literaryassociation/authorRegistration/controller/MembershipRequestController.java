package upp.team5.literaryassociation.authorRegistration.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upp.team5.literaryassociation.authorRegistration.dto.MembershipRequestDTO;
import upp.team5.literaryassociation.authorRegistration.service.MembershipRequestService;

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

    @GetMapping
    public ResponseEntity<List<MembershipRequestDTO>> getRequests() {
        List<MembershipRequestDTO> requestDTOS = this.membershipRequestService.getAllRequests();
        return new ResponseEntity<>(requestDTOS, HttpStatus.OK);
    }
}
