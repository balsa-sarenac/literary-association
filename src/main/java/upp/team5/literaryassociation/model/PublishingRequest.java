package upp.team5.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PublishingRequest{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean approved;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @OneToMany(mappedBy = "publishingRequest", fetch = FetchType.LAZY)
    private Set<Note> notes;

}
