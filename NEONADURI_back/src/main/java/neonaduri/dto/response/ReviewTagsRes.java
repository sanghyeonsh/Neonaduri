package neonaduri.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewTagsRes {

    private final Long reviewId;
    private final String reviewContent;
    private final String reviewImage;
    private final List<String> tagContents;


    @Builder
    public ReviewTagsRes(Long reviewId, String reviewContent, String reviewImage, List<String> tagContents) {
        this.reviewId = reviewId;
        this.reviewContent = reviewContent;
        this.reviewImage = reviewImage;
        this.tagContents = tagContents;
    }
}
