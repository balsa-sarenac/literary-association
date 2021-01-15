
package upp.team5.literaryassociation.publishing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upp.team5.literaryassociation.common.dto.FileDTO;
import upp.team5.literaryassociation.common.dto.UserDTO;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO{
    private Long id;
    private String title;
    private String synopsis;
    private List<UserDTO> authors;
    private FileDTO bookFile;
}
