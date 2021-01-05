package upp.team5.literaryassociation.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Role;
import upp.team5.literaryassociation.security.repository.RoleRepository;

@Service
@Slf4j
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getByName(String role) { return roleRepository.findByName(role); }

}
