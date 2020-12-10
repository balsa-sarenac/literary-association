package upp.team5.literaryassociation.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.dto.RegistrationDTO;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class RegistrationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> register(RegistrationDTO regDTO) {
        var u = userRepository.getUserByEmail(regDTO.getEmail());
        if(u == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setEnabled(false);
        user.setCity(regDTO.getCity());
        user.setCountry(regDTO.getCountry());
        user.setEmail(regDTO.getEmail());
        user.setFirstName(regDTO.getFirstName());
        user.setLastName(regDTO.getLastName());
        user.setPassword(passwordEncoder.encode(regDTO.getPassword()));

        Set<Role> rolesSet = new HashSet<Role>();
        var role = roleRepository.findByName(regDTO.getRole());
        rolesSet.add(role);
        user.setRoles(rolesSet);
        
        this.userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
