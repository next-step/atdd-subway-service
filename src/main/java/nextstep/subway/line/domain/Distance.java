package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private static final String ERROR_MESSAGE_EXCEEDED_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    private void isValidDistance(Distance newDistance) {
        if (this.distance <= newDistance.distance) {
            throw new RuntimeException(ERROR_MESSAGE_EXCEEDED_DISTANCE);
        }
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Distance subtract(Distance newDistance) {
        isValidDistance(newDistance);
        return new Distance(this.distance - newDistance.getDistance());
    }

}
