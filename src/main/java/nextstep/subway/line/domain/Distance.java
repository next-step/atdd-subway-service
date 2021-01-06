package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.exception.InvalidDistanceException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Distance {
    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    public Distance plusDistance(Distance newDistance) {
        return new Distance(this.distance + newDistance.getDistance());
    }

    public Distance minusDistance(Distance newDistance) {
        if (isLessOrEqual(newDistance)) {
            throw new InvalidDistanceException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        return new Distance(this.distance - newDistance.getDistance());
    }

    private boolean isLessOrEqual(Distance newDistance) {
        return this.distance <= newDistance.getDistance();
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
