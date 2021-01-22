package upp.team5.literaryassociation.camunda.type;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import upp.team5.literaryassociation.model.FileDB;

import java.util.HashSet;
import java.util.List;

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
    public String convertModelValueToFormValue(Object o) {
        if (o != null) {
            if (!(o instanceof List)) {
                throw new ProcessEngineException("Model value should be a List");
            }
        }

        return o.toString();
    }


    @Override
    protected TypedValue convertValue(TypedValue propertyValue) {
        if(propertyValue instanceof FileValue) {
            return propertyValue;
        } else {
            Object value = propertyValue.getValue();
            if(value == null) {
                return Variables.fileValue("null", propertyValue.isTransient()).create();
            } else {
                return Variables.stringValue(value.toString(), propertyValue.isTransient());
            }
        }
    }
}
