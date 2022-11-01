package neonaduri.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SpotContentRes {

    private final String spotName;

    private final Float lat;

    private final Float lng;

    private final String spotContent;

    private final String spotImage;

    @Builder
    SpotContentRes(String spotName, Float lat, Float lng, String spotContent, String spotImage){
        this.spotName = spotName;
        this.lat = lat;
        this.lng = lng;
        this.spotContent = spotContent;
        this.spotImage = spotImage;
    }
}
