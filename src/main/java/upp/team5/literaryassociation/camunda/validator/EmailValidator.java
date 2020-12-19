package upp.team5.literaryassociation.camunda.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

@Component
public class EmailValidator implements FormFieldValidator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean validate(Object o, FormFieldValidatorContext formFieldValidatorContext) {
        String email = (String) o;

//        User user = this.userRepository.getUserByEmail(email);
//        return user != null;
        return email.matches("^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$");
    }
}
