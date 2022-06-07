package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Distance {

    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    protected Distance() {
    }

    public static Distance sum(Distance d1, Distance d2) {
        requireNonNull(d1, "d1");
        requireNonNull(d2, "d2");
        return new Distance(d1.distance + d2.distance);
    }

    public void minus(Distance other) {
        if (this.distance <= other.distance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.distance -= other.distance;
    }

    public void plus(Distance other) {
        this.distance += other.distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
