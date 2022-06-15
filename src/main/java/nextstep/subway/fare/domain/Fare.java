package nextstep.subway.fare.domain;

import java.util.Objects;

public class Fare {
    private static final int MIN_NUM = 0;
    private final int fare;

    private Fare(int fare) {
        validateFare(fare);
        this.fare = fare;
    }

    public static Fare valueOf(int fare) {
        return new Fare(fare);
    }

    private void validateFare(int fare) {
        if (fare < MIN_NUM) {
            throw new IllegalArgumentException("음수는 유효하지 않습니다.");
        }
    }

    public int fare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
