package upp.team5.literaryassociation.form.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.camunda.bpm.engine.form.FormField;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldsDTO {
    private String processId;
    private String taskId;
    private List<FormField> formFieldList;

}
