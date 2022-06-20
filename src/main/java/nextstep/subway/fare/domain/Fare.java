package nextstep.subway.fare.domain;

import nextstep.subway.fare.application.InvalidFareException;

import java.util.Objects;

public class Fare {
    private static final int MIN = 0;
    private int value;

    public Fare() {
        this.value = MIN;
    }

    public Fare(int value) {
        if (value < 0) {
            throw new InvalidFareException("요금은 음수일 수 없습니다.");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
