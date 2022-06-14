package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    public static final int MIN_DISTANCE = 0;

    @Column
    private final int distance;

    public Distance(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException("거리는 0보다 작을 수 없습니다.");
        }
        this.distance = distance;
    }

    public Distance() {
        distance = MIN_DISTANCE;
    }

    public Distance plus(Distance distance) {
        return new Distance(this.distance + distance.getDistance());
    }

    public Distance minus(Distance distance) {
        if (this.distance <= distance.getDistance()) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        return new Distance(this.distance - distance.getDistance());
    }

    public int getDistance() {
        return distance;
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
