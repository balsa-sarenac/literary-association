package upp.team5.literaryassociation.authorMembershipPayment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.authorMembershipPayment.service.MembershipPaymentService;
import upp.team5.literaryassociation.form.dto.FormSubmissionDTO;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(path = "/membership-payment")
public class MembershipPaymentController {

    private final MembershipPaymentService membershipPaymentService;

    @Autowired
    MembershipPaymentController(MembershipPaymentService membershipPaymentService){
        this.membershipPaymentService = membershipPaymentService;
    }

    @PostMapping(name = "payMembershipFee", path = "/pay/{processId}")
    public void payMembershipFee(@RequestBody FormSubmissionDTO formSubmissionDTO, @PathVariable String processId) {
        membershipPaymentService.payFee(processId);
    }
}
