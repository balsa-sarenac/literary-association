package upp.team5.literaryassociation.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public User getUserByEmail(String email);
}
