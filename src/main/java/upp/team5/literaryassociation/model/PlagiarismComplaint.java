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
public class PlagiarismComplaint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User complainant;

    @ManyToOne
    private Book complainantBook;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book plagiarism;

    @OneToMany(mappedBy = "plagiarismComplaint")
    private Set<Note> notes;

    @ManyToMany(mappedBy = "complaintsToInvestigate")
    private Set<User> editorsOnInvestigation;
}
