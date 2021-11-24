package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    @Column
    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public Distance add(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public Distance subtract(Distance newDistance) {
        if (this.distance <= newDistance.distance) {
            throw new DistanceSplitFaildException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");
        }
        return new Distance(distance - newDistance.distance);
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
