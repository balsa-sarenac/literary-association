package upp.team5.literaryassociation.file.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(produces = "application/json", path = "/file")
public class FileController {
    @PostMapping(name="upload", path = "/upload/{processId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upload(@RequestParam("files") MultipartFile file, @PathVariable String processId){
        log.info("Uploading files");
    }
}
