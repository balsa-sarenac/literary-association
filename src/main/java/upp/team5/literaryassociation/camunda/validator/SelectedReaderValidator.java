package upp.team5.literaryassociation.camunda.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import upp.team5.literaryassociation.model.FileDB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class SelectedReaderValidator implements FormFieldValidator {
    @Override
    public boolean validate(Object submittedValue, FormFieldValidatorContext validatorContext) {
        Map<String, String> props = validatorContext.getFormFieldHandler().getProperties();
        String dependsOn="";
        Boolean empty=false;
        ArrayList<FileDB> readers = new ArrayList<>();
        if(submittedValue != null) {
            if (!submittedValue.equals("")) {
                readers.addAll((ArrayList<FileDB>) submittedValue);
            }
        }
        if(props.containsKey("controlledBy")){
            dependsOn=props.get("controlledBy");

        }
        if(!dependsOn.equals("")){
            empty = (Boolean) validatorContext.getVariableScope().getVariable(dependsOn);
            //empty = variable.equals("")?false:Boolean.parseBoolean(variable);
        }

        if((empty && readers.size()>=1) || !empty){
            return true;
        }
        return false;
    }
}
