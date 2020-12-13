package upp.team5.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MembershipRequest {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column
      private boolean feePaid;

      @OneToOne(mappedBy = "membershipRequest")
      private User author;

      @OneToMany(mappedBy = "membershipRequest")
      private Set<Document> documents;


}