package upp.team5.literaryassociation.camunda.validator;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import upp.team5.literaryassociation.config.SpringContext;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

@Slf4j
public class EmailValidator implements FormFieldValidator {

    private UserRepository userRepository;

    public EmailValidator() {
        this.userRepository = SpringContext.getBean(UserRepository.class);
    }

    @Override
    public boolean validate(Object o, FormFieldValidatorContext formFieldValidatorContext) {
        String email = (String) o;
        log.info("Checking if email is in correct format");
        if (!email.matches("^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$")) {
            return false;
        }

        log.info("Checking if email already exists");
        User user = this.userRepository.getUserByEmail(email);
        return user == null;
    }
}
