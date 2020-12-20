package upp.team5.literaryassociation.register.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import upp.team5.literaryassociation.model.Genre;
import upp.team5.literaryassociation.register.repository.GenreRepository;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository){
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
    public Genre getGenreByName(String name) { return genreRepository.findByName(name); }

}
