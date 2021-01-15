package upp.team5.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String synopsis;

    @ManyToMany(mappedBy = "authorBooks")
    private Set<User> authors;

    @ManyToMany(mappedBy = "lectorBooks")
    private Set<User> lectors;

    @ManyToMany(mappedBy = "editorBooks")
    private Set<User> editors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_editor_id", referencedColumnName = "id")
    private User chiefEditor;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private Set<PublishingRequest> publishingRequests;

    @ManyToMany
    @JoinTable(name = "books_genres",
        joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genres;

    @OneToOne
    private FileDB bookFile;

}
