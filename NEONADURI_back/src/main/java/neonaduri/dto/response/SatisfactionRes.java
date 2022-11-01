package neonaduri.dto.response;

import lombok.Getter;
import neonaduri.domain.statistics.Satisfaction;
import neonaduri.domain.type.SatisfactionType;

@Getter
public class SatisfactionRes {

    private final Long satId;

    private final Integer satYear;

    private final String satRegion;

    private final Float satScore;

    private final String satType;

    public SatisfactionRes(Satisfaction satisfaction) {
        this.satId = satisfaction.getSatId();
        this.satYear = satisfaction.getSatYear();
        this.satRegion = satisfaction.getSatRegion();
        this.satScore = satisfaction.getSatScore();
        this.satType = satisfaction.getSatType();
    }
}
