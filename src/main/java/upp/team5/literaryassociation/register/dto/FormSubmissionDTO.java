package upp.team5.literaryassociation.register.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FormSubmissionDTO {
    private List<FormSubmissionFieldDTO> formFields;
}
