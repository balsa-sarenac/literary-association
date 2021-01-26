package upp.team5.literaryassociation.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private UserDTO user;
    private String content;
    private String noteType;
    private DateTime dateTime;
}
