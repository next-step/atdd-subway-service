package nextstep.subway.line.domain;

import nextstep.subway.ErrorMessage;

import javax.persistence.Embeddable;

@Embeddable
public class TempDistance {

    private int tempDistance;

    public TempDistance(int distance) {
        this.tempDistance = distance;
    }

    protected TempDistance() {

    }

    public int getTempDistance() {
        return tempDistance;
    }

    public void subtract(TempDistance newDistance) {
        if (this.tempDistance <= newDistance.getTempDistance()) {
            throw new RuntimeException(ErrorMessage.EXCEED_SECTION_DISTANCE.getMessage());
        }
        this.tempDistance -= newDistance.getTempDistance();
    }
}
