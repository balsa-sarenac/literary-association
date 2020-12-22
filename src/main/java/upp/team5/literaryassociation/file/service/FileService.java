package upp.team5.literaryassociation.file.service;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

@Service
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


            fileDBRepository.save(fileDB);

            toMap.add(fileDB);
        }

        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();

        runtimeService.setVariable(processId, "files", toMap);
        runtimeService.setVariable(processId, "loggedUser", task.getAssignee());
        HashMap<String, Object> map = listToMap(toMap);

        formService.submitTaskForm(task.getId(), map );
    }

    private HashMap<String, Object> listToMap(HashSet<FileDB> files) {
        HashMap<String, Object> map = new HashMap<>(); //obrisati
        map.put("files", files);
        return map;
    }
}
