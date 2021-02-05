package upp.team5.literaryassociation.camunda.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import upp.team5.literaryassociation.register.service.GenreService;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import java.util.List;

public class GenresValidator implements FormFieldValidator {

    @Override
    public boolean validate(Object o, FormFieldValidatorContext formFieldValidatorContext) {
        if (o instanceof List)
            return ((List<String>) o).size() >= 1;
        else
            return false;

    }
}
