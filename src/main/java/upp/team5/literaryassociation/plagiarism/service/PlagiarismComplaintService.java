package upp.team5.literaryassociation.plagiarism.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.task.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import upp.team5.literaryassociation.common.dto.FileDTO;
import upp.team5.literaryassociation.common.dto.PlagiarismComplaintDTO;
import upp.team5.literaryassociation.common.dto.UserDTO;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.*;
import upp.team5.literaryassociation.plagiarism.repository.PlagiarismComplaintRepository;
import upp.team5.literaryassociation.publishing.service.BookService;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;
import upp.team5.literaryassociation.security.service.RoleService;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlagiarismComplaintService {
    @Autowired
    private PlagiarismComplaintRepository plagiarismComplaintRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUserService authUserService;

    public PlagiarismComplaint getPlagiarismComplaint(Long id){
        return plagiarismComplaintRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plagiarism complaint with given id doesn't exist"));
    }

    public PlagiarismComplaint save(PlagiarismComplaint plagiarismComplaint) {
        return plagiarismComplaintRepository.save(plagiarismComplaint);
    }

    public List<UserDTO> getEditorsThatCanLeaveNotes(Long complaintId) {
        var plagiarismComplaint = getPlagiarismComplaint(complaintId);
        Role role = roleService.getByName("ROLE_EDITOR");
        List<User> editors = userRepository.findAllByRolesIn(List.of(role));

        List<User> editorsThatHaveLeftNotes = null;
        if (plagiarismComplaint.getNotes() != null) {
            editorsThatHaveLeftNotes = plagiarismComplaint.getNotes()
                    .stream().map(Note::getUser).collect(Collectors.toList());
        }

        if (editorsThatHaveLeftNotes != null) {
            editors.removeIf(editorsThatHaveLeftNotes::contains);
        }

        List<UserDTO> editorDTOs = new ArrayList<>();
        for (User user : editors) {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            editorDTOs.add(userDTO);
        }

        return editorDTOs;
    }

    public List<PlagiarismComplaintDTO> getComplaints() {
        User user = authUserService.getLoggedInUser();
        PlagiarismComplaintStage stage = switch (user.getRoles().iterator().next().getName()) {
            case "ROLE_CHIEF_EDITOR" -> PlagiarismComplaintStage.CHOOSE_EDITORS;
            case "ROLE_EDITOR" -> PlagiarismComplaintStage.EDITORS_LEAVE_NOTES;
            case "ROLE_COMMITTEE_MEMBER" -> PlagiarismComplaintStage.COMMITTEE_VOTING;
            default -> null;
        };

        List<PlagiarismComplaint> complaints = plagiarismComplaintRepository.findAllByPlagiarismComplaintStage(stage);
        List<PlagiarismComplaintDTO> complaintDTOS = complaints.stream()
                .map(plagiarismComplaint -> modelMapper.map(plagiarismComplaint, PlagiarismComplaintDTO.class))
                .collect(Collectors.toList());

        return complaintDTOS;
    }

    public PlagiarismComplaintDTO getComplaint(Long complaintId) {
        PlagiarismComplaint plagiarismComplaint = getPlagiarismComplaint(complaintId);
        PlagiarismComplaintDTO plagiarismComplaintDTO = modelMapper.map(plagiarismComplaint, PlagiarismComplaintDTO.class);

        FileDB complainantBook = plagiarismComplaint.getComplainantBook().getBookFile();
        FileDB plagiarismBook = plagiarismComplaint.getPlagiarism().getBookFile();

        if (complainantBook == null || plagiarismBook == null) {
//            throw new BpmnError("BOOK_FILES_DONT_EXIST");
            return plagiarismComplaintDTO;
        }

        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/membership-requests/documents/")
                .path(String.valueOf(complainantBook.getId()))
                .toUriString();
        FileDTO fileDTO = new FileDTO(
                complainantBook.getName(),
                fileDownloadUri,
                complainantBook.getType(),
                complainantBook.getData().length);
        plagiarismComplaintDTO.getComplainantBook().setBookFile(fileDTO);

        fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/membership-requests/documents/")
                .path(String.valueOf(plagiarismBook.getId()))
                .toUriString();
        fileDTO = new FileDTO(
                plagiarismBook.getName(),
                fileDownloadUri,
                plagiarismBook.getType(),
                plagiarismBook.getData().length);
        plagiarismComplaintDTO.getPlagiarism().setBookFile(fileDTO);

        return plagiarismComplaintDTO;
    }
}
