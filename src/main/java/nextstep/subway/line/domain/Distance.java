package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    @Column(name = "distance")
    private int value;

    protected Distance() {
    }

    public Distance(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 1) {
            throw new IllegalArgumentException("구간 길이는 0보다 커야 합니다.");
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isNotLargerThan(Distance distanceNew) {
        return value <= distanceNew.value;
    }

    public Distance plus(Distance distanceNew) {
        return new Distance(value + distanceNew.value);
    }
    
    public Distance minus(Distance distance) {
        if (this.isNotLargerThan(distance)) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }

        return new Distance(this.value - distance.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
