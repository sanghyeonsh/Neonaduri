package neonaduri.dto.response;

import lombok.Getter;
import neonaduri.domain.statistics.Selected;

@Getter
public class SelectedRes {

    private final Long selId;

    private final String selReason;

    private final Integer visitedYear;

    private final Float selPercent;

    public SelectedRes(Selected selected) {
        this.selId = selected.getSelId();
        this.selReason = selected.getSelReason();
        this.visitedYear = selected.getVisitedYear();
        this.selPercent = selected.getSelPercent();
    }

}
