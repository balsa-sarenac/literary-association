package upp.team5.literaryassociation.plagiarism.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.dto.PlagiarismComplaintDTO;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.plagiarism.repository.PlagiarismComplaintRepository;
import upp.team5.literaryassociation.publishing.service.BookService;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import javax.ws.rs.NotFoundException;

@Service
@Slf4j
public class PlagiarismComplaintService {
    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private PlagiarismComplaintRepository plagiarismComplaintRepository;


    public PlagiarismComplaint getPlagiarismComplaint(Long id){
        return plagiarismComplaintRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plagiarism complaint with given id doesn't exist"));
    }

    public PlagiarismComplaint save(PlagiarismComplaint plagiarismComplaint) {
        return plagiarismComplaintRepository.save(plagiarismComplaint);
    }
}
