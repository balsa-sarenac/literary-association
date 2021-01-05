package upp.team5.literaryassociation.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(String email);

    List<User> findAllByEnabledAndRolesIn(boolean enabled, List<Role> roles);

    @Query( "select u from User u inner join u.roles r where r.name in :roles" )
    List<User> findBySpecificRoles(@Param("roles") List<Role> roles);

    List<User> findByRoles_Id(Long id);

    List<User> findAllByRolesIn(List<Role> roles);

    User findByEmail(String email);

    User findByMembershipRequest_Id(Long id);
}
