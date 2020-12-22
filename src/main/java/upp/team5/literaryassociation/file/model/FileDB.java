package upp.team5.literaryassociation.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileDB {
    @Id
    @GeneratedValue(generator = "uuid")
    private String id;

    private String name;

    private String type;

    @Lob
    private byte[] data;
}
