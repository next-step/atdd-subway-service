package nextstep.subway.fare;

import java.util.Objects;

public class Distance {
    private static final int MIN = 0;

    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isLessThan(Distance targetDistance) {
        return distance - targetDistance.distance <= MIN;
    }

    public Distance minus(Distance distance) {
        if (this.distance - distance.getDistance() <= MIN) {
            throw new IllegalArgumentException();
        }
        return new Distance(this.distance - distance.getDistance());
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
