package upp.team5.literaryassociation.camunda.validator;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import upp.team5.literaryassociation.model.Book;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.PublishingRequest;

import java.util.HashSet;

public class PdfOnlyValidator implements FormFieldValidator {
    @Override
    public boolean validate(Object submittedValue, FormFieldValidatorContext validatorContext) {
        HashSet<FileDB> files = (HashSet<FileDB>) submittedValue;

        if(files!=null){
            for(FileDB file : files){

                if(!file.getType().equals("application/pdf")){
                    return false;
                }

            }
        }


        return true;
    }
}
