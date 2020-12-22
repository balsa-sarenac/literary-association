package upp.team5.literaryassociation.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upp.team5.literaryassociation.file.model.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

}