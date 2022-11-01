package neonaduri.api.repository;

import com.amazonaws.services.s3.AmazonS3Client;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import neonaduri.domain.QClassification;
import neonaduri.dto.request.SearchSpotReq;
import neonaduri.dto.response.QSearchSpotDto;
import neonaduri.dto.response.SearchSpotDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static neonaduri.domain.QClassification.classification;
import static neonaduri.domain.QRegion.*;
import static neonaduri.domain.QSpot.*;
import static org.springframework.util.StringUtils.hasText;

public class SpotRepositoryImpl implements SpotRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final AmazonS3Client amazonS3Client;
    public SpotRepositoryImpl(EntityManager em,AmazonS3Client amazonS3Client) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public Page<SearchSpotDto> searchByCon(SearchSpotReq searchSpotReq, Pageable pageable) {
        List<SearchSpotDto> content = jpaQueryFactory
                .select(new QSearchSpotDto(
                        spot.spotId,
                        spot.spotImage,
                        spot.spotContent,
                        spot.spotName,
                        spot.lat,
                        spot.lng,
                        spot.tel))
                .from(spot, region, classification)
                .where(spotRegionJoin(),
                        spotClassJoin(),
                        spotNameContain(searchSpotReq.getKeyword()),
                        spotSigunguEq(searchSpotReq.getSigungu()),
                        spotSidoEq(searchSpotReq.getSido()),
                        spotClassIn(searchSpotReq.getClassification())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<SearchSpotDto> countQuery = jpaQueryFactory
                .select(
                        Projections.constructor(SearchSpotDto.class,
                                spot.spotId,
                                spot.spotImage, //spot/AC01
                                spot.spotContent,
                                spot.spotName,
                                spot.lat,
                                spot.lng,
                                spot.tel
                        ))
                .from(spot, region, classification)
                .where(spotRegionJoin(),
                        spotClassJoin(),
                        spotNameContain(searchSpotReq.getKeyword()),
                        spotSigunguEq(searchSpotReq.getSigungu()),
                        spotSidoEq(searchSpotReq.getSido()),
                        spotClassIn(searchSpotReq.getClassification()));

        return PageableExecutionUtils.getPage(content,pageable,countQuery::fetchCount);
    }

    private BooleanExpression spotClassJoin() {
        return spot.classId.eq(classification.classId);
    }

    private BooleanExpression spotClassIn(String classReq) {
        List<String> classList = null;
        if(hasText(classReq)) {
            String[] classArray = classReq.split(",");
            classList = new ArrayList<>(Arrays.asList(classArray));
        }

        return classList != null ? classification.smClass.in(classList) : null;
    }

    private BooleanExpression spotSidoEq(String sido) {
        return hasText(sido) ? region.sido.eq(sido) : null;
    }

    private BooleanExpression spotRegionJoin() {
        return spot.regionId.eq(region.regionId);
    }



    private BooleanExpression spotSigunguEq(String sigungu) {
        return hasText(sigungu) ? region.sigungu.eq(sigungu) : null;
    }

    private BooleanExpression spotNameContain(String keyword) {
        return hasText(keyword) ? spot.spotName.contains(keyword) : null;
    }
}
