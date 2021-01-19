package upp.team5.literaryassociation.common.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FormSubmissionDTO {
    private List<FormSubmissionFieldDTO> formFields;
}
