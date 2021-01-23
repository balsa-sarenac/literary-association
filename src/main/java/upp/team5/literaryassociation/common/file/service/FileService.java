package upp.team5.literaryassociation.common.file.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import upp.team5.literaryassociation.common.dto.FileDTO;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.common.file.repository.FileDBRepository;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.BookService;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;
import upp.team5.literaryassociation.security.repository.UserRepository;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class FileService {
    @Autowired
    private FileDBRepository fileDBRepository;

    @Autowired
    private FormService formService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublishingRequestService publishingRequestService;

    public void store(MultipartFile[] files, String processId) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if(principal instanceof UserDetails){
            username = ((UserDetails)principal).getUsername();
        }
        else{
            username = principal.toString();
        }

        User dbUser = userRepository.getUserByEmail(username);

        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        var pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        var p = runtimeService.getVariables(processId);

        if(task.getTaskDefinitionKey()=="SubmitDocuments"){
            dbUser.setStatus("reviewExpected");
        }

        HashSet<FileDB> toMap = new HashSet<>();
        for (MultipartFile file: files) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

            if(task.getTaskDefinitionKey().equals("SubmitDocuments"))
                fileDB.setMembershipRequest(dbUser.getMembershipRequest());
            else if(task.getTaskDefinitionKey().equals("UploadBook") || task.getName().equals("Upload book for review") || task.getTaskDefinitionKey().equals("change-book")){
                var request = runtimeService.getVariable(processId, "publishing-request-id");
                PublishingRequest publishingRequest = publishingRequestService.getPublishingRequest(Long.parseLong(request.toString()));

                publishingRequest.setStatus("Book uploaded");
                publishingRequestService.savePublishingRequest(publishingRequest);

                //var book = publishingRequest.getBook();
                //book.setBookFile(fileDB);
                //bookService.saveBook(book);

                fileDB.setPublishingRequest(publishingRequest);
                fileDB.setUploadedBookId(publishingRequest.getBook().getId());
            }
            toMap.add(fileDB);
        }

        fileDBRepository.saveAll(toMap);

//        runtimeService.setVariable(processId, "files", toMap);

        HashMap<String, Object> map = listToMap(toMap);

        formService.submitTaskForm(task.getId(), map );
    }

    @Transactional
    public FileDB getByBookId(Long id){
        return fileDBRepository.findByUploadedBookId(id);
    }

    private HashMap<String, Object> listToMap(HashSet<FileDB> files) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("files", files);
        return map;
    }

    public void updateFiles(HashSet<FileDB> files) {
        log.info("Saving files to repository");
        fileDBRepository.saveAll(files);
    }

    public FileDB findById(Long id) {
        return this.fileDBRepository.findById(id).orElseThrow(
                () -> new NotFoundException("File with given id doesn't exits!"));
    }

    @Transactional
    public List<FileDB> findAllByMembershipRequest(MembershipRequest membershipRequest) {
        return this.fileDBRepository.findAllByMembershipRequest(membershipRequest);
    }

    public void saveFile(FileDB fileDB) {
        this.fileDBRepository.save(fileDB);
    }

    @Transactional
    public List<FileDB> findAllByPublishingRequest(PublishingRequest request) {
        return this.fileDBRepository.findAllByPublishingRequest(request);
    }

    @Transactional
    public List<FileDTO> getAllDTOByPublishingRequest(PublishingRequest request){
        var files = fileDBRepository.findAllByPublishingRequest(request);
        return generateFileDTOList(files);
    }

    private List<FileDTO> generateFileDTOList(List<FileDB> files) {
        ModelMapper modelMapper = new ModelMapper();
        List<FileDTO> newFiles = new LinkedList<>();
        for(FileDB file : files){
            newFiles.add(modelMapper.map(file, FileDTO.class));
        }
        return newFiles;
    }
}
