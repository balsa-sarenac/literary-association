package upp.team5.literaryassociation.security.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.exception.BadRequestException;
import upp.team5.literaryassociation.exception.UserNotFoundException;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.common.dto.RequestDTO;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service @Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.userRepository.getUserByEmail(s);

        if (user == null) throw new UsernameNotFoundException("Could not find user with given username/email");

        return user;
    }

    public ResponseEntity<Void> enable(Long userId) {
        User user = getUser(userId);

        if (user.isEnabled()) throw new BadRequestException("User with given id has already been enabled");

        user.setEnabled(true);
        this.userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> disable(Long userId) {
        User user = getUser(userId);

        if (!user.isEnabled()) throw new BadRequestException("User with given id has already been disabled");

        user.setEnabled(false);
        this.userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User getUser(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User getUserByEmail(String email) { return userRepository.getUserByEmail(email); }

    public List<User> getUsersByRole(Long id) { return userRepository.findByRoles_Id(id); }

    public void saveUser(User user) { userRepository.save(user); }

    public ResponseEntity<List<RequestDTO>> getRegistrationRequests() {
        log.info("Searching for all registration requests");
        List<Role> roles = this.roleRepository.findAllByNameIn(List.of("ROLE_LECTOR", "ROLE_EDITOR"));
        List<User> users = this.userRepository.findAllByEnabledAndRolesIn(false, roles);

        List<RequestDTO> requests = new ArrayList<>();
        for (User user : users) {
            ModelMapper modelMapper = new ModelMapper();
            RequestDTO requestDTO = modelMapper.map(user, RequestDTO.class);
            Set<Role> userRoles = user.getRoles();
            if (userRoles.size() != 0)
                requestDTO.setRole(userRoles.iterator().next().getName().substring(5).toLowerCase().replace('_', ' '));

            requests.add(requestDTO);
        }
        log.info("Returning list of requests");
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    public ResponseEntity<?> delete(Long userId) {
        log.info("Deleting user with given id");
        this.userRepository.deleteById(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
