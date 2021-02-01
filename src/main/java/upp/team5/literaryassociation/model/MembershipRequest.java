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
public class MembershipRequest implements Serializable {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column
      private boolean active;

      @Column
      private boolean feePaid;

      @Column
      private int voteRound;

      @OneToOne
      private User author;

      @OneToMany(mappedBy = "membershipRequest", cascade = CascadeType.ALL)
      private Set<FileDB> documents;

      @OneToMany(mappedBy = "membershipRequest", cascade = CascadeType.ALL)
      private Set<Vote> votes;

      @OneToMany(mappedBy = "membershipRequest")
      private Set<Note> notes;

}
