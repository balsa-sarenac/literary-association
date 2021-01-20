package upp.team5.literaryassociation.publishing.betaReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.common.dto.NoteDTO;
import upp.team5.literaryassociation.common.dto.PublishingRequestDTO;
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
    public ResponseEntity<List<PublishingRequestDTO>> getBetaBooks() {
        List<PublishingRequestDTO> requestDTOS = this.publishingRequestService.getBetaRequests();
        return new ResponseEntity<>(requestDTOS, HttpStatus.OK);
    }

    @GetMapping(path = "/requests/{id}", name = "all requests that this beta reader can read")
    public ResponseEntity<PublishingRequestDTO> getBetaBook(@PathVariable Long id) {
        PublishingRequestDTO requestDTO = this.publishingRequestService.getBetaRequest(id);
        return new ResponseEntity<>(requestDTO, HttpStatus.OK);
    }

}
