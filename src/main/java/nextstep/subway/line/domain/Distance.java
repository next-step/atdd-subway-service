package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {

    }

    public Distance(int distance) {
        if (distance < 1) {
            throw new IllegalArgumentException("거리는 양수값 이어야 합니다.");
        }

        this.distance = distance;
    }

    public Distance subtract(int newDistance) {
        return new Distance(this.distance -= newDistance);
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
