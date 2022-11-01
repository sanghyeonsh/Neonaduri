package neonaduri.api.repository;

import neonaduri.domain.Region;
import neonaduri.dto.response.MyeonResponseDto;
import neonaduri.dto.response.SidoResponseDto;
import neonaduri.dto.response.SigunguResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("select new neonaduri.dto.response.SidoResponseDto(r.sido) from Region r group by r.sido")
    List<SidoResponseDto> findAllSido();

    @Query("select new neonaduri.dto.response.SigunguResponseDto(r.sigungu) from Region r where r.sido = :sido group by r.sigungu")
    List<SigunguResponseDto> findSigunguBySido(@Param("sido") String sido);

    @Query("select new neonaduri.dto.response.MyeonResponseDto(r.regionId,r.myeon) from Region r where r.sigungu = :sigungu and r.sido = :sido")
    List<MyeonResponseDto> findMyeonBySigungu(@Param("sido") String sido, @Param("sigungu") String sigungu);
    
}
