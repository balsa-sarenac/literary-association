package upp.team5.literaryassociation.publishing.service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import upp.team5.literaryassociation.common.dto.BookDTO;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.repository.BookRepository;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import java.util.HashSet;

@Service
@Slf4j
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomUserDetailsService userService;

    public void saveBook(Book book) { bookRepository.save(book);}

    public HashSet<BookDTO> getAuthorBooks(String authorId) {
        ModelMapper modelMapper = new ModelMapper();
        HashSet<BookDTO> retVal = new HashSet<>();

        User author = userService.getUser(Long.parseLong(authorId));
        HashSet<Book> books = bookRepository.findByAuthors(author);

        for(Book book:books){
            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
            retVal.add(bookDTO);
        }
        return retVal;
    }
}
