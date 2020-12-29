package upp.team5.literaryassociation.form.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.form.service.GenericFormService;
import upp.team5.literaryassociation.form.dto.FormFieldsDTO;
import upp.team5.literaryassociation.form.dto.FormSubmissionDTO;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/form")
public class FormController {

    private GenericFormService genericFormService;

    @Autowired
    public FormController(GenericFormService genericFormService){
        this.genericFormService = genericFormService;
    }

    @GetMapping(name = "getForm", path="/get/{processInstanceId}")
    public ResponseEntity<?> getFrom(@PathVariable String processInstanceId) throws JsonProcessingException {
        FormFieldsDTO formFieldsDTO = genericFormService.getForm(processInstanceId);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(formFieldsDTO);
        log.info(json);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping(name = "submitForm", path = "/submit/{processInstanceId}")
    public void submitForm(@RequestBody FormSubmissionDTO formSubmissionDTO, @PathVariable String processInstanceId) {
        genericFormService.submitForm(processInstanceId, formSubmissionDTO);
    }
}

