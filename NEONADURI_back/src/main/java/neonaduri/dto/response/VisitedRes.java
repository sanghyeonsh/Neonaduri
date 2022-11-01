package neonaduri.dto.response;

import lombok.Getter;
import neonaduri.domain.statistics.Visited;

@Getter
public class VisitedRes {

    private final Long visitedId;

    private final String visitedRegion;

    private final Integer visitedYear;

    private final Integer visitedNum;

    public VisitedRes(Visited visited) {
        this.visitedId = visited.getVisitedId();
        this.visitedRegion = visited.getVisitedRegion();
        this.visitedYear = visited.getVisitedYear();
        this.visitedNum = visited.getVisitedNum();
    }
}
