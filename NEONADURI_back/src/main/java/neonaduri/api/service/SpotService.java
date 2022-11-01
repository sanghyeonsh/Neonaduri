package neonaduri.api.service;

import lombok.RequiredArgsConstructor;
import neonaduri.api.repository.SpotRepository;
import neonaduri.domain.Review;
import neonaduri.domain.Spot;
import neonaduri.domain.Tag;
import neonaduri.dto.request.SearchSpotReq;
import neonaduri.dto.response.ReviewTagsRes;
import neonaduri.dto.response.SearchSpotDto;
import neonaduri.dto.response.SpotDetailsRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final RecommendService recommendService;


    public SpotDetailsRes getSpotDetailsInfo(Long spotId) {

        /* 1. fetch join을 통해 review까지 가져와서 */
        Spot spot = spotRepository.findDetailsSpotBySpotId(spotId);

        /* 2. review 목록에서 리뷰 내용고 태그를 꺼내와서 ReviewTagsRes로 정리해준다. */
        Set<Review> reviewSet = Optional.of(spot.getReviews()).orElse(new HashSet<>());
        List<ReviewTagsRes> reviews = reviewSet.stream()
                .map(review -> ReviewTagsRes.builder()
                        .reviewId(review.getReviewId())
                        .reviewContent(review.getReviewContent())
                        .reviewImage(review.getReviewImage())
                        .tagContents(review.getTags().stream()
                                .map(Tag::getTagContent)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        /* 3. Spot 정보를 캐시를 해서 Recommend Service에 넣어준다*/
        recommendService.cachingSpotData(spotId);

        /* 3. 마지막으로 SpotDetailsRes로 반환시켜준다. */
        return SpotDetailsRes.builder()
                .spotContent(spot.getSpotContent())
                .spotName(spot.getSpotName())
                .spotImage(spot.getSpotImage())
                .reviewContainsTags(reviews)
                .build();
    }

    public Page<SearchSpotDto> searchSpotService(SearchSpotReq searchSpotReq, Pageable pageable) {
        return spotRepository.searchByCon(searchSpotReq, pageable);
    }

    @Transactional
    public void putSpotContent(Long spotId, String spotContent) {
        Spot spot = spotRepository.findSpotBySpotId(spotId);
        spot.changeSpotContent(spotContent);
    }
}
