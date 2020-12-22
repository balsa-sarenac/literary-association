package upp.team5.literaryassociation.file.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upp.team5.literaryassociation.file.service.FileService;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/file")
public class FileController {
    @Autowired
    private FileService storageService;

    @PostMapping(name="upload", path = "/upload/{processId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @PathVariable String processId){
        log.info("Uploading files");
        try {
            storageService.store(file);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
