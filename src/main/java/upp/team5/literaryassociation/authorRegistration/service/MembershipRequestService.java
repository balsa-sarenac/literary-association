package upp.team5.literaryassociation.authorRegistration.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import upp.team5.literaryassociation.authorRegistration.dto.MembershipRequestDTO;
import upp.team5.literaryassociation.authorRegistration.repository.MembershipRequestRepository;
import upp.team5.literaryassociation.common.dto.FileDTO;
import upp.team5.literaryassociation.common.dto.UserDTO;
import upp.team5.literaryassociation.common.file.service.FileService;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MembershipRequestService {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private MembershipRequestRepository membershipRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;

    public void addNewRequest(HashSet<FileDB> files, String processId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if(principal instanceof UserDetails){
             username = ((UserDetails)principal).getUsername();
        }
        else{
             username = principal.toString();
        }

        org.camunda.bpm.engine.identity.User user = identityService.createUserQuery().userEmail(username).singleResult();

        upp.team5.literaryassociation.model.User dbUser = userRepository.getUserByEmail(username);

        MembershipRequest membershipRequest = new MembershipRequest();

        membershipRequest.setAuthor(dbUser);
        membershipRequest.setFeePaid(false);
        membershipRequest.setActive(true);

        if (membershipRequest.getDocuments() == null) {
            membershipRequest.setDocuments(new HashSet<>());
        }

        for (FileDB file : files) {
            membershipRequest.getDocuments().add(file);
        }

        membershipRequest = membershipRequestRepository.save(membershipRequest);

        dbUser.setMembershipRequest(membershipRequest);

        this.userRepository.save(dbUser);

//        fileService.updateFiles(files, membershipRequest);
    }

    public List<MembershipRequestDTO> getAllRequests() {
        log.info("Retrieving all requests from database");
        List<MembershipRequest> requests = this.membershipRequestRepository.findAllByActive(true);

        List<MembershipRequestDTO> requestDTOS = new ArrayList<>();
        for (MembershipRequest request : requests) {
            MembershipRequestDTO membershipRequestDTO = new MembershipRequestDTO();
            User user = request.getAuthor();
            UserDTO userDTO = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(),
                    user.getCity(), user.getCountry(), user.getEmail());
            membershipRequestDTO.setUser(userDTO);
            requestDTOS.add(membershipRequestDTO);
        }

        return requestDTOS;
    }

    public MembershipRequestDTO getRequest(Long id) {
        log.info("Retrieving membership request with id: {}", id);
        MembershipRequest membershipRequest = this.membershipRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Membership request with given id doesn't exist"));

        UserDTO userDTO = modelMapper.map(membershipRequest.getAuthor(), UserDTO.class);

        List<FileDTO> files = membershipRequest.getDocuments().stream().map(file -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/document/")
                    .path(file.getId())
                    .toUriString();

            return new FileDTO(
                    file.getName(),
                    fileDownloadUri,
                    file.getType(),
                    file.getData().length);
        }).collect(Collectors.toList());

        MembershipRequestDTO membershipRequestDTO = new MembershipRequestDTO();
        membershipRequestDTO.setId(membershipRequest.getId());
        membershipRequestDTO.setUser(userDTO);
        membershipRequestDTO.setFiles(files);

        return membershipRequestDTO;
    }
}
