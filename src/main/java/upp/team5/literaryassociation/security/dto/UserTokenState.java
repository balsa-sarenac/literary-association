package upp.team5.literaryassociation.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTokenState {
    private Long id;
    private String  accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String username;
    private String role;
}