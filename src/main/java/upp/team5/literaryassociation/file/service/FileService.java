package upp.team5.literaryassociation.file.service;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import upp.team5.literaryassociation.file.model.FileDB;
import upp.team5.literaryassociation.file.repository.FileDBRepository;
import upp.team5.literaryassociation.form.dto.FormSubmissionFieldDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class FileService {
    @Autowired
    private FileDBRepository fileDBRepository;

    @Autowired
    private FormService formService;

    @Autowired
    private TaskService taskService;



    public void store(MultipartFile[] files, String processId) throws IOException {

        HashSet<FileDB> toMap = new HashSet<>();
        for (MultipartFile file: files) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

            toMap.add(fileDB);
            fileDBRepository.save(fileDB);
        }

        HashMap<String, Object> map = listToMap(toMap);
        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
        formService.submitTaskForm(task.getId(), map );
    }

    private HashMap<String, Object> listToMap(HashSet<FileDB> files) {
        HashMap<String, Object> map = new HashMap<>(); //obrisati
        map.put("files", files);
        return map;
    }
}
