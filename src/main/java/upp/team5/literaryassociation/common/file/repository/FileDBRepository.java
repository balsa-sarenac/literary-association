package upp.team5.literaryassociation.common.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upp.team5.literaryassociation.model.FileDB;
import upp.team5.literaryassociation.model.MembershipRequest;
import upp.team5.literaryassociation.model.PublishingRequest;

import java.util.List;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, Long> {

    List<FileDB> findAllByMembershipRequest(MembershipRequest membershipRequest);

    List<FileDB> findAllByPublishingRequest(PublishingRequest request);

    FileDB findByUploadedBookId(Long bookID);

}