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
public class Genre {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column
      private String name;

//      private Set<User> users;

      @ManyToMany(mappedBy = "betaGenres")
      private Set<User> betaReaders;

      @ManyToMany(mappedBy = "genres")
      private Set<Book> books;

}
