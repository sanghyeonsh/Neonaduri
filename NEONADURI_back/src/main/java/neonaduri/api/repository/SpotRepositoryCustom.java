package neonaduri.api.repository;

import neonaduri.dto.request.SearchSpotReq;
import neonaduri.dto.response.SearchSpotDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpotRepositoryCustom {
    Page<SearchSpotDto> searchByCon(SearchSpotReq searchSpotReq, Pageable pageable);
}
