package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {
    private static final String INVALID_FARE_ERROR_MESSAGE = "노선의 추가요금은 0 이상만 가능합니다.";

    @Column(name = "fare", nullable = false)
    private int fare;

    protected Fare() {}

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int fare) {
        validateFare(fare);
        return new Fare(fare);
    }

    private static void validateFare(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException(INVALID_FARE_ERROR_MESSAGE);
        }
    }

    public int getValue() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Fare fare1 = (Fare)o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
