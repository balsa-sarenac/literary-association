package upp.team5.literaryassociation.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private String country;
    private String role;
}
