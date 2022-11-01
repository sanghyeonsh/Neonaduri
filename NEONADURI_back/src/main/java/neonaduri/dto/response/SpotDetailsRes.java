package neonaduri.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SpotDetailsRes {

    private final String spotContent;

    private final String spotName;

    private final String spotImage;

    private final List<ReviewTagsRes> reviewContainsTags;

    @Builder
    public SpotDetailsRes(String spotContent, String spotName, String spotImage, List<ReviewTagsRes> reviewContainsTags) {
        this.spotContent = spotContent;
        this.spotName = spotName;
        this.spotImage = spotImage;
        this.reviewContainsTags = reviewContainsTags;
    }
}
