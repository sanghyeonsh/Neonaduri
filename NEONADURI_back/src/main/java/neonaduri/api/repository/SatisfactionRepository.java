package neonaduri.api.repository;

import neonaduri.domain.statistics.Satisfaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatisfactionRepository extends JpaRepository<Satisfaction, Long> {

    Satisfaction findSatisfactionBySatId(Long satId);

}
