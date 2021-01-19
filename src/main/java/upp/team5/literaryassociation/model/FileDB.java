package upp.team5.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "files")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileDB implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_request_id", referencedColumnName = "id")
    private MembershipRequest membershipRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publishing_request_id", referencedColumnName = "id")
    private PublishingRequest publishingRequest;

    @OneToOne
    private Book book;

    @Column
    private Long bookId;

    public FileDB(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
