package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {
    private static final int ZERO = 0;
    private static final String CAN_NOT_NEGATIVE = "요금은 음수일 수 없습니다.";

    private final int value;

    public Fare(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < ZERO) {
            throw new IllegalArgumentException(CAN_NOT_NEGATIVE);
        }
    }

    public Fare add(Fare fare) {
        return new Fare(this.value + fare.value);
    }

    public Fare subtract(Fare fare) {
        return new Fare(this.value - fare.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
