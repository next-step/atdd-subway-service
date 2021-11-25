package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    @Column(name = "distance")
    private int value;

    protected Distance() {
        this.value = 0;
    }

    private Distance(Integer value) {
        this.validateCreate(value);

        this.value = value;
    };

    private void validateCreate(Integer value) {
        if (value <= 0) {
            throw new IllegalArgumentException("구간의 거리는 0이상이어야 합니다.");
        }
    }

    public static Distance of(Integer value) {
        return new Distance(value);
    }

    public Distance plus(Distance distance) {
        return Distance.of(this.value + distance.value);
    }

    public Distance minus(Distance distance) {
        this.validateMinus(distance);
        return Distance.of(this.value - distance.value);
    }

    private void validateMinus(Distance distance) {
        if (this.value < distance.value) {
            throw new IllegalArgumentException("기 등록된 구간의 거리보다 큰 거리로 뺄 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Distance)) {
            return false;
        }
        Distance distance = (Distance) o;
        return Objects.equals(value, distance.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
