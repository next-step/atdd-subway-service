package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int MIN_DISTANCE = 0;

    @Column
    private int distance;

    protected Distance() {

    }

    private Distance(int distance) {
        valid(distance);
        this.distance = distance;
    }

    public static Distance of(int distance) {
        return new Distance(distance);
    }

    public boolean isEqualAndLess(Distance compareSource) {
        return this.value() <= compareSource.value();
    }

    public void minusDistance(Distance distance) {
        minusDistance(distance.value());
    }

    public void minusDistance(int number) {
        valid(this.distance - number);
        this.distance -= number;
    }

    public Distance subDistance(Distance distance) {
        Distance source = Distance.of(this.value());
        source.minusDistance(distance);
        return source;
    }

    public int rateDistance(Distance distance) {
        if (Objects.isNull(distance)) {
            throw new IllegalArgumentException("나눌 거리는 0이면 안됩니다.");
        }

        if (this.value() % distance.value() == 0) {
            return this.value() / distance.value();
        }
        return this.value() / distance.value() + 1;
    }

    public static Distance sumDistance(Distance source, Distance target) {
        return Distance.of(source.value() + target.value());
    }

    private void valid(int distance) {
        if (MIN_DISTANCE >= distance) {
            throw new IllegalArgumentException("거리는 0보다 커야 합니다.");
        }
    }

    public int value() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Distance)) {
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
