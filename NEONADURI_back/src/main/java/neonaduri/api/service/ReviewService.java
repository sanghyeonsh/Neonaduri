package neonaduri.api.service;

import lombok.RequiredArgsConstructor;
import neonaduri.api.repository.ReviewRepository;
import neonaduri.api.repository.SpotRepository;
import neonaduri.domain.Review;
import neonaduri.domain.Tag;
import neonaduri.dto.request.CreateReviewReq;
import neonaduri.dto.request.ModifyReviewReq;
import neonaduri.dto.response.ReviewTagsRes;
import neonaduri.utils.S3Utils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final S3Utils s3Utils;
    private final SpotRepository spotRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;

    public void postReview(CreateReviewReq createReviewReq) throws IOException {
        /* Tag 먼저 생성 */
        Set<Tag> tags = Arrays.stream(createReviewReq.getTags().split(", "))
                .map(Tag::new)
                .collect(Collectors.toSet());
        String uuid = UUID.randomUUID().toString();
        String dirName = "review/" + uuid;
        s3Utils.upload(createReviewReq.getReviewImage(), dirName);

        /* Review 생성하기 */
        Review review = Review.builder()
                .spotId(spotRepository.findSpotBySpotId(createReviewReq.getSpotId()))
                .tags(tags)
                .reviewContent(createReviewReq.getReviewContent())
                .reviewDate(LocalDateTime.now())
                .reviewImage(dirName+"/"+createReviewReq.getReviewImage().getOriginalFilename())
                .reviewPassword(passwordEncoder.encode(createReviewReq.getReviewPassword()))
                .build();

        /* Review DB 반영 */
        reviewRepository.save(review);
    }

    public void putReview(ModifyReviewReq modifyReviewReq, MultipartFile reviewImage) throws IOException {

        Review review = reviewRepository.findReviewByReviewId(modifyReviewReq.getReviewId());
        Set<Tag> tags = Arrays.stream(modifyReviewReq.getTags()
                        .split(", "))
                .map(Tag::new)
                .collect(Collectors.toSet());
        String dirName = review.getReviewImage().split("/")[0]+"/"+review.getReviewImage().split("/")[1];
        Optional<MultipartFile> reviewImageOp = Optional.ofNullable(reviewImage);
        if(reviewImageOp.isPresent()) {
            s3Utils.upload(reviewImageOp.get(), dirName);
            review.modifyReview(tags, modifyReviewReq.getReviewContent(), LocalDateTime.now(),
                    dirName+"/"+reviewImage.getOriginalFilename());
        } else {
            review.modifyReview(tags, modifyReviewReq.getReviewContent(), LocalDateTime.now(),
                    review.getReviewImage());
        }
    }

    public ReviewTagsRes getReviewDetailsInfo(Long reviewId){

        Review review = reviewRepository.findReviewByReviewId(reviewId);
        ReviewTagsRes reviewTagsRes = ReviewTagsRes.builder()
                .reviewId(review.getReviewId())
                .reviewContent(review.getReviewContent())
                .reviewImage(review.getReviewImage())
                .tagContents(review.getTags().stream()
                        .map(Tag::getTagContent)
                        .collect(Collectors.toList()))
                .build();

        return reviewTagsRes;

    }


    public boolean comparePass(Long reviewId, String password) {
        Review review = reviewRepository.findReviewByReviewId(reviewId);
        if (passwordEncoder.matches(password, review.getReviewPassword())) {
            return true;
        }
        return false;
    }

    public void deleteReview(Long reviewId){
        reviewRepository.deleteById(reviewId);
    }

}
