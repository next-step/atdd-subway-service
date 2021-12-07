package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    protected static final int MIN_DISTANCE = 0;
    private static final String SHORTER_THAN_MIN_DISTANCE = "지하철 구간 사이의 거리는 " + MIN_DISTANCE + "보다 커야 합니다.";
    private static final String LONG_DISTANCE_BETWEEN_SECTION = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    private int distance;

    public Distance() {
    }

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= MIN_DISTANCE) {
            throw new IllegalArgumentException(SHORTER_THAN_MIN_DISTANCE);
        }
    }

    public boolean divisible(Distance divideDistance) {
        if (distance <= divideDistance.getDistance()) {
            throw new IllegalArgumentException(LONG_DISTANCE_BETWEEN_SECTION);
        }
        return true;
    }

    public Distance addDistance(Distance newDistance) {
        return new Distance(this.distance + newDistance.distance);
    }

    public Distance minusDistance(Distance newDistance) {
        return new Distance(this.distance - newDistance.distance);
    }

    private void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }
}
