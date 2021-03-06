package upp.team5.literaryassociation.camunda.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

import java.util.HashMap;
import java.util.Map;

public class ExplanationValidator implements FormFieldValidator {
    @Override
    public boolean validate(Object submittedValue, FormFieldValidatorContext validatorContext) {
        Map<String, String> props = validatorContext.getFormFieldHandler().getProperties();
        String dependsOn="";
        Boolean empty=false;
        if(props.containsKey("controlledBy")){
            dependsOn=props.get("controlledBy");

        }
        if(!dependsOn.equals("")){
            empty = (Boolean) validatorContext.getVariableScope().getVariable(dependsOn);
        }

        return (submittedValue != null && !empty && !String.valueOf(submittedValue).isBlank()) || empty;
    }
}
