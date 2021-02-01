package upp.team5.literaryassociation.publishing.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.file.service.FileService;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.service.BookService;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

@Service
@Slf4j
public class ProcessPublishing implements JavaDelegate {
    @Autowired
    private PublishingRequestService publishingRequestService;
    @Autowired
    private FileService fileService;
    @Autowired
    private BookService bookService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Publishing started");

        var requestId = delegateExecution.getVariable("publishing-request-id");
        var request = publishingRequestService.getById(Long.parseLong(requestId.toString()));


        if(request.isPresent()){
            PublishingRequest publishingRequest = request.get();

            publishingRequest.setStatus("Book is published");
            publishingRequestService.savePublishingRequest(publishingRequest);

            log.info("Associating book with file");
            Book publishedBook = publishingRequest.getBook();
            FileDB bookFile = fileService.getByBookId(publishedBook.getId());
            publishedBook.setBookFile(bookFile);
            bookService.saveBook(publishedBook);
            bookFile.setBook(publishedBook);
            fileService.saveFile(bookFile);
        }

        log.info("Publishing done");
    }
}
