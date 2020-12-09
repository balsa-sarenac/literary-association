package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/reg")
public class RegistrationController {

    @Autowired
    public RegistrationController() {

    }

    @PostMapping(name = "register", path = "/register")
    public ResponseEntity<?> register()  {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

