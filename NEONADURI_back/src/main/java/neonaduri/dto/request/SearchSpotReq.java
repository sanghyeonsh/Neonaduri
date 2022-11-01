package neonaduri.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchSpotReq {
    private String sido;
    private String sigungu;
    private String keyword;
    private String classification;
}
