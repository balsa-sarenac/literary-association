package upp.team5.literaryassociation.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldsDTO {
    private String processId;
    private String taskId;
    private List<FormField> formFieldList;

}
