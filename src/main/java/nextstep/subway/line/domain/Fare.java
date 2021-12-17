package nextstep.subway.line.domain;

import nextstep.subway.line.exception.FareException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare {
    private static final String FARE_MIN_SIZE_MESSAGE = "추가요금은 음수일 수 없습니다.";
    private static final int FARE_MIN_VALUE = 0;
    @Column(name = "extraFare")
    private int value;

    protected Fare() {

    }

    private Fare(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < FARE_MIN_VALUE) {
            throw new FareException(FARE_MIN_SIZE_MESSAGE);
        }
    }

    public static Fare of(int value) {
        return new Fare(value);
    }

    public Fare plus(Fare fare) {
        return new Fare(value + fare.getValue());
    }

    public Fare minus(Fare fare) {
        return new Fare(value - fare.getValue());
    }

    public Fare multiply(double rate) {
        return Fare.of((int) (this.value * rate));
    }

    public int getValue() {
        return value;
    }

    public static int compareTo(Fare source, Fare target) {
        return (source.getValue() < target.getValue()) ? -1 : ((source == target) ? 0 : 1);
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
