package upp.team5.literaryassociation.camunda.validator;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

@Slf4j
public class EmailValidator implements FormFieldValidator {

    @Override
    public boolean validate(Object o, FormFieldValidatorContext formFieldValidatorContext) {
        String email = (String) o;
        log.info("Checking if email is in correct format");
        return email.matches("^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$");
    }
}
