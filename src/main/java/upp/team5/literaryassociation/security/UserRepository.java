package upp.team5.literaryassociation.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import upp.team5.literaryassociation.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public User getUserByEmail(String email);
}
