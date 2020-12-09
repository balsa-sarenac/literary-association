package upp.team5.literaryassociation.camunda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @Slf4j
public class CamundaController {

    @GetMapping(path = "camunda-welcome")
    public ResponseEntity<?> welcome() {
        log.info("hello, friend!");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
