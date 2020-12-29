package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.*;
import upp.team5.literaryassociation.register.service.GenreService;
import upp.team5.literaryassociation.security.repository.RoleRepository;
import upp.team5.literaryassociation.security.repository.UserRepository;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;

import java.util.*;

@Service
@Slf4j
public class ChooseEditorService implements JavaDelegate {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomUserDetailsService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private  PublishingRequestService publishingRequestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        User currentUser = null;
        var context = SecurityContextHolder.getContext();
        if(context.getAuthentication() != null) {
            var principal = context.getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String email = ((UserDetails) principal).getUsername();
                currentUser = userService.getUserByEmail(email);
            }
        }

        Role role = roleRepository.findByName("ROLE_EDITOR");
        Role newRole = roleRepository.findByName("ROLE_CHIEF_EDITOR");

        List<User> editors = userService.getUsersByRole(role.getId());
        int rand = getRandomNumber(0, editors.size()-1);
        User chief = editors.get(rand);

        Set<Role> currentRoles = chief.getRoles();
        currentRoles.add(newRole);
        chief.setRoles(currentRoles);

        userService.saveUser(chief);

        HashMap<String, Object> bookInfoSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-fill-book-info");

        Book book = new Book();
        book.setTitle(bookInfoSubmission.get("title").toString());
        book.setSynopsis(bookInfoSubmission.get("synopsis").toString());
        book.setChiefEditor(chief);
        Set<User> authors = new HashSet<>();
        authors.add(currentUser);
        book.setAuthors(authors);
        Genre genre = genreService.getGenreByName(bookInfoSubmission.get("genre").toString());
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        book.setGenres(genres);


        bookService.saveBook(book);

        PublishingRequest req = new PublishingRequest();
        req.setBook(book);
        publishingRequestService.savePublishingRequest(req);

        Set<PublishingRequest> requests = new HashSet<>();
        requests.add(req);
        book.setPublishingRequests(requests);
        bookService.saveBook(book);

        delegateExecution.setVariable("publishing-request-id", req.getId());

    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
