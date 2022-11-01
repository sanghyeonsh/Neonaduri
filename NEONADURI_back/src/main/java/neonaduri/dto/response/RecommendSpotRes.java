package neonaduri.dto.response;

import lombok.Getter;

@Getter
public class RecommendSpotRes {

    private final Long spotId;

    private final String spotName;

    private final String spotContent;

    private final String spotImage;

    public RecommendSpotRes(Long spotId, String spotName, String spotContent, String spotImage) {
        this.spotId = spotId;
        this.spotName = spotName;
        this.spotContent = spotContent;
        this.spotImage = spotImage;
    }
}
