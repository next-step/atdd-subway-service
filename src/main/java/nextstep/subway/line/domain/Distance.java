package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    
    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public Distance getAddedDistance(Distance mergingDistance) {
        return new Distance(distance + mergingDistance.distance);
    }

    public Distance getRemainedDistance(Distance newDistance) {
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
