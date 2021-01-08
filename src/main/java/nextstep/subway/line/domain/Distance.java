package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Distance {

    @Transient
    private static final int MIN_DISTANCE = 0;
    private int distance;

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int newDistance) {
        if (newDistance <= MIN_DISTANCE) {
            throw new IllegalArgumentException("거리는 0 또는 음수가 될 수 없습니다.");
        }

        if (this.distance <= newDistance) {
            throw new IllegalArgumentException("구간을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없습니다.");
        }
    }

    public Distance addDistance(Distance newDistance) {
        return new Distance(this.distance += newDistance.distance);
    }

    public Distance minusDistance(Distance newDistance) {
        return new Distance(this.distance -= newDistance.distance);
    }

    public int get() {
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
