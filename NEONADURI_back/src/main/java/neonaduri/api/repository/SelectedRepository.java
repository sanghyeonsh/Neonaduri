package neonaduri.api.repository;

import neonaduri.domain.statistics.Selected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectedRepository extends JpaRepository<Selected, Long> {
}
