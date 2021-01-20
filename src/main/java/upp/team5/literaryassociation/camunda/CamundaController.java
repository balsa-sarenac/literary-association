package upp.team5.literaryassociation.camunda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController @Slf4j
public class CamundaController {

    private final CamundaService camundaService;

    @Autowired
    public CamundaController(CamundaService camundaService) {
        this.camundaService = camundaService;
    }

    @GetMapping(path = "camunda-welcome")
    public ResponseEntity<?> welcome() {
        log.info("hello, friend!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/processInstanceId/{requestType}/{publishingRequestId}")
    public ResponseEntity<?> getProcessInstanceId(@PathVariable String requestType, @PathVariable Long publishingRequestId) {
        return new ResponseEntity<>(camundaService.getProcessInstanceId(requestType, publishingRequestId), HttpStatus.OK);
    }
}
