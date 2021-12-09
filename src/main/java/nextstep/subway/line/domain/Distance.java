package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    protected static final int MIN_DISTANCE = 1;
    private static final String SHORTER_THAN_MIN_DISTANCE = "지하철 구간 사이의 거리는 " + MIN_DISTANCE + "보다 커야 합니다.";
    private static final String LONG_DISTANCE_BETWEEN_SECTION = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException(SHORTER_THAN_MIN_DISTANCE);
        }
    }

    public boolean divisible(Distance divideDistance) {
        if (distance <= divideDistance.getDistance()) {
            throw new IllegalArgumentException(LONG_DISTANCE_BETWEEN_SECTION);
        }
        return true;
    }

    public Distance plus(Distance newDistance) {
        return new Distance(this.distance + newDistance.distance);
    }

    public Distance minus(Distance newDistance) {
        return new Distance(this.distance - newDistance.distance);
    }

    private void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
