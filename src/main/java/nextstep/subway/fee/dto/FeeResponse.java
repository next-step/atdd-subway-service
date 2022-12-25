package nextstep.subway.fee.dto;

import nextstep.subway.fee.domain.StationFee;

public class FeeResponse {

    private Long id;
    private int applyStartDistance;
    private int applyEndDistance;
    private int applyDistance;
    private int applyFee;

    public FeeResponse() {}

    public FeeResponse(Long id, int applyStartDistance, int applyEndDistance, int applyDistance, int applyFee) {
        this.id = id;
        this.applyStartDistance = applyStartDistance;
        this.applyEndDistance = applyEndDistance;
        this.applyDistance = applyDistance;
        this.applyFee = applyFee;
    }

    public Long getId() {
        return this.id;
    }

    public static FeeResponse of(StationFee stationFee) {
        return new FeeResponse(stationFee.getId(), stationFee.getApplyStartDistance(), stationFee.getApplyEndDistance()
                                ,stationFee.getApplyDistance(), stationFee.getApplyFee());
    }
}
