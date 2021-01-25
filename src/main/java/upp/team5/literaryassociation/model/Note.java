package upp.team5.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Note implements Serializable {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      private String content;

      private boolean deleted;

      @Enumerated(value = EnumType.STRING)
      private NoteType type;

      @ManyToOne
      @JoinColumn(name = "publishing_request_id", referencedColumnName = "id")
      private PublishingRequest publishingRequest;

      @ManyToOne
      @JoinColumn(name = "user_id", referencedColumnName = "id")
      private User user;

      @ManyToOne
      @JoinColumn(name = "membership_request_id")
      private MembershipRequest membershipRequest;

      @ManyToOne
      private PlagiarismComplaint plagiarismComplaint;
}
