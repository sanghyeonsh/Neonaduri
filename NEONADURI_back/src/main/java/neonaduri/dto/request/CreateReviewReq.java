package neonaduri.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import neonaduri.domain.Tag;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @ToString
public class CreateReviewReq {

    @NotNull
    private final Long spotId;

    @NotNull
    private final MultipartFile reviewImage;

    @NotNull
    private final String reviewContent;

    @Size(min = 4, max = 100)
    private final String reviewPassword;

    private final String tags;

    @Builder
    public CreateReviewReq(
            Long spotId, MultipartFile reviewImage, String reviewContent, String reviewPassword, String tags) {
        this.spotId = spotId;
        this.reviewImage = reviewImage;
        this.reviewContent = reviewContent;
        this.reviewPassword = reviewPassword;
        this.tags = tags;
    }

}
