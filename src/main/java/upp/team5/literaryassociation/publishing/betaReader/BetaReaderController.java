package upp.team5.literaryassociation.publishing.betaReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upp.team5.literaryassociation.common.dto.PublishingRequestBetaDTO;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "beta-readers")
public class BetaReaderController {

    private final PublishingRequestService publishingRequestService;

    @Autowired
    public BetaReaderController(PublishingRequestService publishingRequestService) {
        this.publishingRequestService = publishingRequestService;
    }

    @GetMapping(path = "/requests", name = "all requests that this beta reader can read")
    public ResponseEntity<List<PublishingRequestBetaDTO>> getBetaBooks() {
        List<PublishingRequestBetaDTO> requestDTOS = this.publishingRequestService.getBetaRequests();
        return new ResponseEntity<>(requestDTOS, HttpStatus.OK);
    }
}
