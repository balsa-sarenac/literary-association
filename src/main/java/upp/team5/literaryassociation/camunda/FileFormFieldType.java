package upp.team5.literaryassociation.camunda;

import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.io.File;

public class FileFormFieldType extends SimpleFormFieldType {
    public final static String FORM_TYPE = "file";

    @Override
    public String getName() {
        return FORM_TYPE;
    }

    @Override
    public Object convertFormValueToModelValue(Object propertyValue) {
        return null;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        return null;
    }


    @Override
    protected TypedValue convertValue(TypedValue propertyValue) {
        if(propertyValue instanceof FileValue) {
            return propertyValue;
        } else {
            Object value = propertyValue.getValue();
            if(value == null) {
                return Variables.fileValue("null").create();
            } else {
                return Variables.fileValue((File) value);
            }
        }
    }
}
