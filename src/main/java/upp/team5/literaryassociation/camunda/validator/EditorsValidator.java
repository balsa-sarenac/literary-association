package upp.team5.literaryassociation.camunda.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import org.springframework.stereotype.Component;
import upp.team5.literaryassociation.model.FileDB;

import java.util.ArrayList;
import java.util.HashSet;

@Component
public class EditorsValidator implements FormFieldValidator {
    @Override
    public boolean validate(Object o, FormFieldValidatorContext formFieldValidatorContext) {
        ArrayList<FileDB> editors = (ArrayList<FileDB>) o;
        int maxEditors = Integer.parseInt(formFieldValidatorContext.getFormFieldHandler().getProperties().get("maxEditors"));
        int minEditors = Integer.parseInt(formFieldValidatorContext.getFormFieldHandler().getProperties().get("minEditors"));

        return editors.size() <= maxEditors && editors.size() >= minEditors;

    }
}
