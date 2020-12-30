package upp.team5.literaryassociation.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Book implements Serializable {

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

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<PublishingRequest> publishingRequests;

    @ManyToMany
    @JoinTable(name = "books_genres",
        joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genres;
}
