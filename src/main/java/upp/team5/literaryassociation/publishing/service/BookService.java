package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.publishing.repository.BookRepository;

@Service
@Slf4j
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public void saveBook(Book book) { bookRepository.save(book);}
}
