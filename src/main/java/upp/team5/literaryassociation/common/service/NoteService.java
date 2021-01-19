package upp.team5.literaryassociation.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.common.repository.NoteRepository;
import upp.team5.literaryassociation.model.Note;
import upp.team5.literaryassociation.model.NoteType;
import upp.team5.literaryassociation.model.User;

import java.util.List;

@Service @Slf4j
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void saveNote(Note note) {
        this.noteRepository.save(note);
    }

    public List<Note> getUserNotes(User user, NoteType type) {
        return this.noteRepository.findAllByUserAndType(user, type);
    }
}
