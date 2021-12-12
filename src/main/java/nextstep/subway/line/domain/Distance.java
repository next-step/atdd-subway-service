package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    public int value() {
        return distance;
    }

    public Distance plus(Distance otherDistance) {
        return new Distance(this.distance + otherDistance.value());
    }

    public Distance minus(Distance otherDistance) {
        return new Distance(this.distance - otherDistance.value());
    }

    public boolean isLessThanEqualTo(Distance otherDistance) {
        return this.distance <= otherDistance.value();
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new BadRequestException("구간의 거리는 1 이상으로 입력해주세요.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance other = (Distance) o;
        return distance == other.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
