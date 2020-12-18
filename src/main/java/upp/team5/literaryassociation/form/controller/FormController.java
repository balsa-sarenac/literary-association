package upp.team5.literaryassociation.form.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<FormFieldsDTO> getFrom(@PathVariable String processInstanceId) {
        FormFieldsDTO formFieldsDTO = genericFormService.getForm(processInstanceId);
        return new ResponseEntity<>(formFieldsDTO, HttpStatus.OK);
    }

    @PostMapping(name = "submitForm", path = "/submit/{processInstanceId}")
    public void submitForm(@RequestBody FormSubmissionDTO formSubmissionDTO, @PathVariable String processInstanceId) {
        genericFormService.submitForm(processInstanceId, formSubmissionDTO);
    }
}

