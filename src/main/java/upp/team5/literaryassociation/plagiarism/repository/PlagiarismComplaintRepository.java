package upp.team5.literaryassociation.plagiarism.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upp.team5.literaryassociation.model.PlagiarismComplaint;

public interface PlagiarismComplaintRepository extends JpaRepository<PlagiarismComplaint, Long> {
}
