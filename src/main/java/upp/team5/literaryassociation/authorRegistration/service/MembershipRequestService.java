package upp.team5.literaryassociation.authorRegistration.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import upp.team5.literaryassociation.authorRegistration.dto.MembershipRequestDTO;
import upp.team5.literaryassociation.authorRegistration.repository.MembershipRequestRepository;
import upp.team5.literaryassociation.common.dto.FileDTO;
import upp.team5.literaryassociation.common.dto.UserDTO;
import upp.team5.literaryassociation.common.file.service.FileService;
import upp.team5.literaryassociation.exception.UserNotFoundException;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.model.Vote;
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

    public void addNewRequest(DelegateExecution delegateExecution) {
        Long id = (Long) delegateExecution.getVariable("userId");
        upp.team5.literaryassociation.model.User dbUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        MembershipRequest membershipRequest = new MembershipRequest();

        membershipRequest.setAuthor(dbUser);
        membershipRequest.setFeePaid(false);
        membershipRequest.setActive(true);
        membershipRequest.setVoteRound(0);

        membershipRequest = membershipRequestRepository.save(membershipRequest);

        dbUser.setMembershipRequest(membershipRequest);

        delegateExecution.setVariable("membershipRequestId", membershipRequest.getId());

        this.userRepository.save(dbUser);
    }

    public List<MembershipRequestDTO> getAllRequests() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if(principal instanceof UserDetails){
            email = ((UserDetails)principal).getUsername();
        }
        else{
            email = principal.toString();
        }

        User committee = this.userRepository.findByEmail(email);

        log.info("Retrieving all requests from database");
        List<MembershipRequest> requests = this.membershipRequestRepository.findAllByActive(true);

        List<MembershipRequestDTO> requestDTOS = new ArrayList<>();
        for (MembershipRequest request : requests) {
            List<Vote> votes = request.getVotes().stream()
                    .filter(vote -> vote.getCommitteeMember().equals(committee)).collect(Collectors.toList());

            if (votes.stream().anyMatch(vote -> vote.getRound() >= request.getVoteRound())) {
                continue;
            }
            MembershipRequestDTO membershipRequestDTO = new MembershipRequestDTO();
            User user = request.getAuthor();
            UserDTO userDTO = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(),
                    user.getCity(), user.getCountry(), user.getEmail());
            membershipRequestDTO.setId(request.getId());
            membershipRequestDTO.setUser(userDTO);
            requestDTOS.add(membershipRequestDTO);
        }

        return requestDTOS;
    }

    public MembershipRequestDTO getRequest(Long id) {
        MembershipRequest membershipRequest = getMembershipRequest(id);

        UserDTO userDTO = modelMapper.map(membershipRequest.getAuthor(), UserDTO.class);


        List<FileDB> requestFiles = null;
        try {
            requestFiles = this.fileService.findAllByMembershipRequest(membershipRequest);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        assert requestFiles != null;
        List<FileDTO> files = requestFiles.stream().map(file -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/membership-requests/documents/")
                    .path(String.valueOf(file.getId()))
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

    public MembershipRequest getMembershipRequest(Long id) {
        log.info("Retrieving membership request with id: {}", id);
        return this.membershipRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Membership request with given id doesn't exist"));
    }

    public ResponseEntity<byte[]> getDocument(Long id) {
        FileDB fileDB = this.fileService.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
}
