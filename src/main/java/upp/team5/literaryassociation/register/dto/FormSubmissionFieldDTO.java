package upp.team5.literaryassociation.register.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FormSubmissionFieldDTO implements Serializable {
    private String id;
    private String value;
}
