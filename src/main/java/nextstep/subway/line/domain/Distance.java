package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int MIN = 1;

    @Column(nullable = false)
    private int value;

    protected Distance() {
    }

    public Distance(int distance) {
        if (distance < MIN) {
            throw new IllegalArgumentException(String.format("구간거리는 %d보다 크거나 같아야합니다.", MIN));
        }

        this.value = distance;
    }

    public int getValue() {
        return value;
    }

    public Distance substract(Distance distance) {
        return new Distance(value - distance.getValue());
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
        return value;
    }
}
