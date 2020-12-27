package upp.team5.literaryassociation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String exception) {
        super(exception);
    }
}
