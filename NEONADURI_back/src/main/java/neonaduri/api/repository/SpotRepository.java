package neonaduri.api.repository;

import neonaduri.domain.Spot;
import neonaduri.dto.response.RecommendSpotRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SpotRepository extends JpaRepository<Spot, Long>, SpotRepositoryCustom {

    Spot findSpotBySpotId(Long spotId); // Optional X. 빈 값 있지 않음.

    RecommendSpotRes findHotSpotBySpotId(Long spotId);

    @Query("select s from Spot s left join fetch s.reviews where s.spotId=:spotId")
    Spot findDetailsSpotBySpotId(Long spotId);
}
