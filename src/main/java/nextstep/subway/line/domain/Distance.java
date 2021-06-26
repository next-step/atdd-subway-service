package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    public static final String INVALID_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    private int value;

    public Distance() {
    }

    public Distance(int value) {
        checkValidValue(value);
        this.value = value;
    }

    public int toInt() {
        return value;
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
