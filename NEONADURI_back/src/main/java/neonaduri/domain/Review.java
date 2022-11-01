package neonaduri.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neonaduri.dto.request.CreateReviewReq;
import neonaduri.dto.request.ModifyReviewReq;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spotId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "review_tag",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate;

    @Column(name = "review_image", nullable = false)
    private String reviewImage;

    @Column(name = "review_password", nullable = false)
    private String reviewPassword;

    @Builder
    public Review(Spot spotId, Set<Tag> tags, String reviewContent, LocalDateTime reviewDate, String reviewImage, String reviewPassword) {
        this.spotId = spotId;
        this.tags = tags;
        this.reviewContent = reviewContent;
        this.reviewDate = reviewDate;
        this.reviewImage = reviewImage;
        this.reviewPassword = reviewPassword;
    }

    public void modifyReview(Set<Tag> tags, String reviewContent, LocalDateTime reviewDate, String reviewImage){
        this.tags = tags;
        this.reviewContent = reviewContent;
        this.reviewDate = reviewDate;
        this.reviewImage = reviewImage;
    }

}
