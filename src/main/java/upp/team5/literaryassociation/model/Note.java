package upp.team5.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Note {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      private String content;

      @Enumerated(value = EnumType.STRING)
      private NoteType type;

      @ManyToOne
      @JoinColumn(name = "publishing_request_id", referencedColumnName = "id")
      private PublishingRequest publishingRequest;

      @ManyToOne
      @JoinColumn(name = "user_id", referencedColumnName = "id")
      private User user;

}
