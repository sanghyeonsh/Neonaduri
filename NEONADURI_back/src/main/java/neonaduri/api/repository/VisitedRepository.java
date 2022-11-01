package neonaduri.api.repository;

import neonaduri.domain.statistics.Visited;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitedRepository  extends JpaRepository<Visited, Long> {
}
