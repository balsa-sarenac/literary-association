package upp.team5.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private VoteOption voteOption;

    @Column
    private DateTime voteTime;

    @Column
    private int round;

    @ManyToOne
    @JoinColumn(name = "membership_request_id", referencedColumnName = "id")
    private MembershipRequest membershipRequest;

    @ManyToOne
    @JoinColumn(name = "committee_id", referencedColumnName = "id")
    private User committeeMember;
}
