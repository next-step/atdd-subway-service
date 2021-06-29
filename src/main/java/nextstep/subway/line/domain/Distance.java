package nextstep.subway.line.domain;

import nextstep.subway.errorMessage.ErrorEnum;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private int value;

    public Distance() {
    }

    public Distance(int value) {
        checkValidValue(value);
        this.value = value;
    }

    public boolean isOver(int value) {
        return value < this.value;
    }

    public int toInt() {
        return value;
    }

    private void checkValidValue(int value) {
        if (value < Sections.SECTION_DELETABLE_MIN_SIZE) {
            throw new RuntimeException(ErrorEnum.INVALID_DISTANCE.message());
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
            throw new RuntimeException(ErrorEnum.INVALID_DISTANCE.message());
        }
        return new Distance(newValue);
    }

    public Distance add(Distance request) {
        return new Distance(value + request.value);
    }
}
