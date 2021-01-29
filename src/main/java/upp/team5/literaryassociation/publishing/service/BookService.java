package upp.team5.literaryassociation.publishing.service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import upp.team5.literaryassociation.common.dto.BookDTO;
import upp.team5.literaryassociation.common.dto.UserDTO;
import upp.team5.literaryassociation.exception.BookNotFoundException;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.repository.BookRepository;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomUserDetailsService userService;

    public void saveBook(Book book) { bookRepository.save(book);}

    @Transactional
    public HashSet<BookDTO> getAuthorBooks(String authorId) {
        ModelMapper modelMapper = new ModelMapper();
        HashSet<BookDTO> retVal = new HashSet<>();

        User author = userService.getUserById(Long.parseLong(authorId));
        HashSet<Book> books = bookRepository.findByAuthors(author);

        for(Book book:books){
            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
            retVal.add(bookDTO);
        }
        return retVal;
    }

    @Transactional
    public HashSet<BookDTO> getOtherAuthorBooks(String authorId) {
        ModelMapper modelMapper = new ModelMapper();
        HashSet<BookDTO> retVal = new HashSet<>();

        User author = userService.getUserById(Long.parseLong(authorId));
        List<Book> books = bookRepository.findAll();

        for(Book book:books){
            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
            UserDTO foundAuthor = bookDTO.getAuthors().stream()
                    .filter(x -> x.getId()==author.getId())
                    .findAny()
                    .orElse(null);
            if(foundAuthor==null){
                retVal.add(bookDTO);
            }
        }
        return retVal;
    }

    @Transactional
    public Book getBook(Long bookId) throws BookNotFoundException {
        return bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    }
}
