package upp.team5.literaryassociation.register.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    private String firstName;

    private String lastName;

    private String city;

    private String country;

    private String email;

    private String password;

    private String role;
}
