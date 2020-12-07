package upp.team5.literaryassociation.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class JwtAuthenticationRequest {
    private String email;
    private String password;
}
