package upp.team5.literaryassociation.publishing.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.model.*;
import upp.team5.literaryassociation.register.service.GenreService;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;
import upp.team5.literaryassociation.security.service.RoleService;

import java.util.*;

@Service
@Slf4j
public class ChooseEditorService implements JavaDelegate {
    @Autowired
    private RoleService roleService;
    @Autowired
    private CustomUserDetailsService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private  PublishingRequestService publishingRequestService;

    @Autowired
    private IdentityService identityService;

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

//        Role role = roleService.getByName("ROLE_EDITOR");
        Role role = roleService.getByName("ROLE_CHIEF_EDITOR");

        List<User> chiefs = userService.getUsersByRole(role.getId());
//        int rand = getRandomNumber(0, editors.size()-1);
        User chief = chiefs.get(0);

        //Set<Role> currentRoles = chief.getRoles();
        //currentRoles.add(newRole);
        //chief.setRoles(currentRoles);

        //userService.saveUser(chief);

        HashMap<String, Object> bookInfoSubmission;
        bookInfoSubmission = (HashMap<String, Object>) delegateExecution.getVariable("data-fill-book-info");

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
        req.setStatus("New request");
        req.setBook(book);
        publishingRequestService.savePublishingRequest(req);

        book.setPublishingRequest(req);
        bookService.saveBook(book);

        var books = currentUser.getAuthorBooks();
        if(books == null) {
            books = new HashSet<>();
        }
        books.add(book);
        currentUser.setAuthorBooks(books);

        userService.saveUser(currentUser);

        org.camunda.bpm.engine.identity.User cUser = identityService.createUserQuery().userId(String.valueOf(chief.getId())).singleResult();
        if(cUser == null){
            createCamundaUser(chief, "asdf");
        }


        delegateExecution.setVariable("publishing-request-id", req.getId());
        delegateExecution.setVariable("editor", chief.getId().toString());
    }

    private int getRandomNumber(int min, int max) {
        if (max == 0) return 0;
        Random random = new Random();
        return  random.nextInt(max - min) + min;
    }

    public void createCamundaUser(upp.team5.literaryassociation.model.User user, String password) {
        org.camunda.bpm.engine.identity.User cUser = identityService.newUser(user.getId().toString());
        cUser.setEmail(user.getEmail());
        cUser.setFirstName(user.getFirstName());
        cUser.setLastName(user.getLastName());
        cUser.setPassword(password);
        identityService.saveUser(cUser);
    }
}
