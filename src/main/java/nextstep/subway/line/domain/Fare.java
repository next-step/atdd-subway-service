package nextstep.subway.line.domain;

import nextstep.subway.constant.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {
    public static final int FREE = 0;
    private static final int MINIMUM = 0;

    @Column(name = "extra_fare", nullable = false)
    private int value;

    protected Fare() {}

    public Fare(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM) {
            throw new IllegalArgumentException(ErrorCode.FARE_BIGGEST_THAN_ZERO.getMessage());
        }
    }

    public int get() {
        return value;
    }
}
