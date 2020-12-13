package upp.team5.literaryassociation.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(String email);

    List<User> findAllByEnabledAndRolesIn(boolean enabled, List<Role> roles);
}
