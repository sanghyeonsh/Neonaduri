package neonaduri.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;


@Data
@NoArgsConstructor
public class SearchSpotDto {
    private Long spotId;
    private String spotImage;
    private String spotContent;
    private String spotName;
    private Float lat;
    private Float lng;
    private String tel;

    @QueryProjection
    @Builder
    public SearchSpotDto(Long spotId, String spotImage, String spotContent, String spotName, Float lat, Float lng, String tel) {
        this.spotId = spotId;
        this.spotImage = spotImage;
        this.spotContent = spotContent;
        this.spotName = spotName;
        this.lat = lat;
        this.lng = lng;
        this.tel = tel;
    }
}
