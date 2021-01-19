package upp.team5.literaryassociation.plagiarism.controller;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upp.team5.literaryassociation.common.dto.BookDTO;
import upp.team5.literaryassociation.common.dto.ProcessDTO;
import upp.team5.literaryassociation.publishing.service.BookService;

import java.util.HashSet;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping( path = "/book")
public class BookController {
    private final BookService bookService;

    BookController(BookService bookService){
        this.bookService = bookService;
    }

    @PreAuthorize("hasAuthority('ROLE_AUTHOR')")
    @GetMapping(name = "getAuthorBooks", path="/get/{authorId}")
    public ResponseEntity<HashSet<BookDTO>> getAuthorBooks(@PathVariable String authorId) {

        HashSet<BookDTO> books = bookService.getAuthorBooks(authorId);

        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
