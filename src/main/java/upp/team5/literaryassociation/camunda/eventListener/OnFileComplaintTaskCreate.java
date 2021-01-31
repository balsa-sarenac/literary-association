package upp.team5.literaryassociation.camunda.eventListener;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.camunda.type.MultiselectFormFieldType;
import upp.team5.literaryassociation.common.dto.BookDTO;
import upp.team5.literaryassociation.common.dto.UserDTO;
import upp.team5.literaryassociation.common.service.AuthUserService;
import upp.team5.literaryassociation.publishing.service.BookService;
import upp.team5.literaryassociation.publishing.service.PublishingRequestService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class OnFileComplaintTaskCreate implements TaskListener {
    @Autowired
    private FormService formService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthUserService userService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
        List<FormField> properties = taskFormData.getFormFields();

        HashSet<BookDTO> books = bookService.getOtherAuthorBooks(String.valueOf(userService.getLoggedInUser().getId()));

        for(FormField field : properties){
            if(field.getId().equals("values")){
                Map<String, String> enumType = ((EnumFormType) field.getType()).getValues();
                enumType.clear();
                enumType.putAll(listToMap(books));
                break;
            }
        }
    }

    private Map<String, String> listToMap(HashSet<BookDTO> books) {
        Map<String, String> map = new HashMap<>();
        for (BookDTO book : books) {
            map.put(book.getId().toString(), book.getTitle());
        }
        return map;
    }
}
