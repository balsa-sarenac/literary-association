package upp.team5.literaryassociation.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Book {

    @Id
    private Long id;

    @ManyToMany(mappedBy = "authorBooks")
    private Set<User> authors;

    @ManyToMany(mappedBy = "lectorBooks")
    private Set<User> lectors;

    @ManyToMany(mappedBy = "editorBooks")
    private Set<User> editors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_editor_id", referencedColumnName = "id")
    private User chiefEditor;
}
