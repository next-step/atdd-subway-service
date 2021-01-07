package nextstep.subway.path.domain;

import java.util.Objects;

public class Distance {

    private static final long MIN_VALUE = 0;

    private final int value;

    private Distance(final int value) {
        this.value = value;
    }

    public static Distance valueOf(final int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("거리 값은 음수 일 수 없습니다.");
        }
        return new Distance(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Distance)) return false;
        final Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
