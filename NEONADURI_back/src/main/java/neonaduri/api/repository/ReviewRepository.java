package neonaduri.api.repository;

import neonaduri.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findReviewByReviewId(Long reviewId);

    Long countBy();

}
