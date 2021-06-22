package nextstep.subway.line.domain;

import java.util.Objects;

public class Distance {
    public static final String INVALID_DISTANCE = "구간이 너무 깁니다.";
    private int value;

    public Distance(int value) {
        checkValidValue(value);
        this.value = value;
    }

    private void checkValidValue(int value) {
        if (value < Sections.SECTION_DELETABLE_MIN_SIZE) {
            throw new RuntimeException(INVALID_DISTANCE);
        }
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

    public Distance diff(Distance request) {
        int newValue = Math.abs(value - request.value);
        if (value < newValue) {
            throw new RuntimeException(INVALID_DISTANCE);
        }
        return new Distance(newValue);
    }

    public Distance add(Distance request) {
        return new Distance(value + request.value);
    }
}
