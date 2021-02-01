package upp.team5.literaryassociation.camunda.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import upp.team5.literaryassociation.model.FileDB;

import java.util.HashSet;

public class SingleFileRequiredValidator implements FormFieldValidator {
    @Override
    public boolean validate(Object submittedValue, FormFieldValidatorContext validatorContext) {
        HashSet<FileDB> files = (HashSet<FileDB>) submittedValue;

        return files.size()==1;
    }
}
