package nextstep.subway.line.domain;

import nextstep.subway.line.exception.FareException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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

    public int getValue() {
        return value;
    }
}
