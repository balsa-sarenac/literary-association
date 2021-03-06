package upp.team5.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Note implements Serializable {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      private String content;

      @Column
      private Boolean deleted;

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

      @Column
      private DateTime dateTime;
}
