package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    protected Distance() {
    }

    public Distance minus(Distance distance) {
        if (this.distance <= distance.distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        return new Distance(this.distance - distance.distance);
    }

    public Distance plus(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public int getDistance() {
        return distance;
    }

    protected void setDistance(int distance) {
        this.distance = distance;
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
