package upp.team5.literaryassociation.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class User implements UserDetails { //, org.camunda.bpm.engine.identity.User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(name = "authors_books",
            joinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id")
    )
    private Set<Book> authorBooks;

    @ManyToMany
    @JoinTable(name = "lector_books",
            joinColumns = @JoinColumn(name = "lector_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id")
    )
    private Set<Book> lectorBooks;

    @ManyToMany
    @JoinTable(name = "editor_books",
            joinColumns = @JoinColumn(name = "editor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id")
    )
    private Set<Book> editorBooks;

    @OneToMany(mappedBy = "chiefEditor", fetch = FetchType.LAZY)
    private Set<Book> chiefEditorOfBooks;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "author_membershipRequest",
        joinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id"))
    private MembershipRequest membershipRequest;

    @ManyToMany
    @JoinTable(name = "user_genre",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(name = "beta_reader_genres",
        joinColumns = @JoinColumn(name = "beta_user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> betaGenres;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Note> notes;

    @Column
    private boolean enabled;

    @Column
    private boolean accountNonExpired;

    @Column
    private boolean accountNonLocked;

    @Column
    private boolean credentialsNonExpired;

    @Column
    private Timestamp lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

//    @Override
//    public void setId(String s) {
//
//    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
