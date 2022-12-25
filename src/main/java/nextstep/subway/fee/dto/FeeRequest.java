package nextstep.subway.fee.dto;

import nextstep.subway.fee.domain.StationFee;

public class FeeRequest {

    private int applyStartDistance;
    private int applyEndDistance;
    private int applyDistance;
    private int applyFee;

    public FeeRequest(int applyStartDistance, int applyEndDistance, int applyDistance, int applyFee) {
        this.applyStartDistance = applyStartDistance;
        this.applyEndDistance = applyEndDistance;
        this.applyDistance = applyDistance;
        this.applyFee = applyFee;
    }

    public int getApplyStartDistance() {
        return applyStartDistance;
    }

    public int getApplyEndDistance() {
        return applyEndDistance;
    }

    public int getApplyDistance() {
        return applyDistance;
    }

    public int getApplyFee() {
        return applyFee;
    }

    public StationFee toStationFee() {
        return new StationFee(applyStartDistance, applyEndDistance, applyDistance, applyFee);
    }
}
