package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final String MINIMUM_DISTANCE_BETWEEN_STATIONS_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public Distance add(Distance newDistance) {
        return new Distance(distance + newDistance.distance);
    }

    public Distance subtract(Distance newDistance) {
        validateSubtractDistance(newDistance.distance);
        return new Distance(distance - newDistance.distance);
    }

    private void validateSubtractDistance(int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException(MINIMUM_DISTANCE_BETWEEN_STATIONS_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
