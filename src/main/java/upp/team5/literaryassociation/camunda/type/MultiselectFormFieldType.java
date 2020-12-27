package upp.team5.literaryassociation.camunda.type;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.util.List;
import java.util.Map;

public class MultiselectFormFieldType extends SimpleFormFieldType {
    public final static String TYPE_NAME = "multiselect";
    protected static Map<String, String> values;

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public MultiselectFormFieldType(Map<String, String> values) {
        this.values = values;
    }

    @Override
    protected TypedValue convertValue(TypedValue propertyValue) throws ProcessEngineException {
        Object value = propertyValue.getValue();

        return value == null
                ? Variables.stringValue(null, propertyValue.isTransient())
                : Variables.stringValue(value.toString(), propertyValue.isTransient());
    }

    @Override
    public Object getInformation(String key) {
        if (key.equals("values")) {
            return values;
        }
        return null;
    }

    public Map<String, String> getValues() {
        return values;
    }

    @Override
    public Object convertFormValueToModelValue(Object o) {
        return o;
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
}
