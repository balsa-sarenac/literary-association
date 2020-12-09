package upp.team5.literaryassociation.security;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
