package nextstep.subway.line.domain;

import nextstep.subway.line.consts.ErrorMessage;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    public static final int MINIMUM_DISTANCE = 1;

    private int value;

    protected Distance() {
    }

    private Distance(int value) {
        validateDistance(value);
        this.value = value;
    }

    public static Distance from(int value) {
        return new Distance(value);
    }

    private void validateDistance(int value) {
        if (value < MINIMUM_DISTANCE) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessage.ERROR_DISTANCE_TOO_SHORT, MINIMUM_DISTANCE, value)
            );
        }
    }

    public void add(Distance distance) {
        validateDistance(this.value + distance.value());
        this.value += distance.value();
    }

    public void subtract(Distance distance) {
        validateDistance(this.value - distance.value());
        this.value -= distance.value();
    }

    public int value() {
        return value;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Distance)) {
            return false;
        }
        return ((Distance)obj).value() == value;
    }
}
