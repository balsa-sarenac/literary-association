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
import upp.team5.literaryassociation.model.PublishingRequest;
import upp.team5.literaryassociation.model.User;
import upp.team5.literaryassociation.publishing.repository.BookRepository;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomUserDetailsService userService;
    @Autowired
    private PublishingRequestService publishingRequestService;

    public void saveBook(Book book) { bookRepository.save(book);}

    @Transactional
    public HashSet<BookDTO> getAuthorBooks(String authorId) {
        ModelMapper modelMapper = new ModelMapper();
        HashSet<BookDTO> retVal = new HashSet<>();

        User author = userService.getUserById(Long.parseLong(authorId));
        HashSet<Book> books = bookRepository.findByAuthors(author);
        List<PublishingRequest> reqs = publishingRequestService.getRequests(author.getId()).stream().filter(x->x.getStatus().equals("Book is published")).collect(Collectors.toList());

        for(PublishingRequest req:reqs){
            BookDTO bookDTO = modelMapper.map(req.getBook(), BookDTO.class);
            retVal.add(bookDTO);
        }
        return retVal;
    }

    @Transactional
    public HashSet<BookDTO> getOtherAuthorBooks(String authorId) {
        ModelMapper modelMapper = new ModelMapper();
        HashSet<BookDTO> retVal = new HashSet<>();

        User author = userService.getUserById(Long.parseLong(authorId));
        List<Book> books = bookRepository.findAll()
                .stream().filter(x -> x.getPublishingRequest().getStatus().equals("Book is published")).collect(Collectors.toList());

        for(Book book:books){
            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
            UserDTO foundAuthor = bookDTO.getAuthors().stream()
                    .filter(x -> x.getId().equals(author.getId()))
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

    public HashSet<BookDTO> getPublishedBooks() {
        ModelMapper modelMapper = new ModelMapper();
        HashSet<BookDTO> retVal = new HashSet<>();

        List<PublishingRequest> reqs = publishingRequestService.getAll().stream().filter(x->x.getStatus().equals("Book is published")).collect(Collectors.toList());

        for(PublishingRequest req:reqs){
            BookDTO bookDTO = modelMapper.map(req.getBook(), BookDTO.class);
            retVal.add(bookDTO);
        }
        return retVal;
    }
}
