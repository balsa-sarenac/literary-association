package upp.team5.literaryassociation.camunda.eventListener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.camunda.type.MultiselectFormFieldType;
import upp.team5.literaryassociation.model.Genre;
import upp.team5.literaryassociation.register.service.GenreService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OnTaskCreate implements TaskListener {

    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private RuntimeService runtimeService;

    private GenreService genreService;

    public OnTaskCreate(GenreService genreService){
        this.genreService = genreService;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
        List<FormField> properties = taskFormData.getFormFields();

        List<Genre> genres = genreService.getAll();

        for(FormField field : properties){
            if(field.getId().equals("genres")){
                MultiselectFormFieldType multiselectType = (MultiselectFormFieldType) field.getType();
                multiselectType.setValues(listToMap(genres));
                break;
            }
        }

    }

    private Map<String, String> listToMap(List<Genre> genres) {
        Map<String, String> map = new HashMap<>();
        for (Genre g : genres) {
            map.put(g.getName(), g.getName());
        }
        return map;
    }

    private List<String> listToList(List<Genre> genres) {
        List<String> list = new LinkedList<>();
        for (Genre g : genres) {
            list.add(g.getName());
        }
        return list;
    }
}
