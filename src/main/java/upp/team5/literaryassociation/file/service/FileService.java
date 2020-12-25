package upp.team5.literaryassociation.file.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.file.repository.FileDBRepository;
import upp.team5.literaryassociation.model.MembershipRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

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



    public void store(MultipartFile[] files, String processId) throws IOException {

        HashSet<FileDB> toMap = new HashSet<>();
        for (MultipartFile file: files) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

            toMap.add(fileDB);
        }

        fileDBRepository.saveAll(toMap);

        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();

        runtimeService.setVariable(processId, "files", toMap);

        HashMap<String, Object> map = listToMap(toMap);

        formService.submitTaskForm(task.getId(), map );
    }

    private HashMap<String, Object> listToMap(HashSet<FileDB> files) {
        HashMap<String, Object> map = new HashMap<>(); //obrisati
        map.put("files", files);
        return map;
    }

    public void updateFiles(HashSet<FileDB> files, MembershipRequest membershipRequest) {
        for(FileDB file:files){
            file.setMembershipRequest(membershipRequest);

            log.info("updated file");
        }
        //fileDBRepository.saveAll(files);
    }
}
