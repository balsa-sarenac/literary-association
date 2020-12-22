package upp.team5.literaryassociation.camunda.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import org.springframework.stereotype.Component;
import upp.team5.literaryassociation.file.model.FileDB;

import java.util.HashSet;

@Component
public class FileValidator implements FormFieldValidator {


    @Override
    public boolean validate(Object o, FormFieldValidatorContext formFieldValidatorContext) {
        HashSet<FileDB> files = (HashSet<FileDB>) o;

        return files.size()>=2;
    }
}