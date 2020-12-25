package upp.team5.literaryassociation.camunda.eventListener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.register.service.GenreService;

@Service
@Slf4j
public class OnTaskCreatePublishBook implements TaskListener {
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private RuntimeService runtimeService;

    private GenreService genreService;

    public OnTaskCreatePublishBook(GenreService genreService){
        this.genreService = genreService;
    }

    @Override
    public void notify(DelegateTask delegateTask) {

    }
}
