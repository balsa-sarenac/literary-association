package upp.team5.literaryassociation.plagiarism.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.PlagiarismComplaint;
import upp.team5.literaryassociation.model.PlagiarismComplaintStage;

import java.util.List;

public interface PlagiarismComplaintRepository extends JpaRepository<PlagiarismComplaint, Long> {
    List<PlagiarismComplaint> findAllByPlagiarismComplaintStage(PlagiarismComplaintStage stage);
}
