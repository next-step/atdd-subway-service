package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.exception.SubwayException;

@Embeddable
public class Fare {

    private static final int MIN_VALUE = 0;

    @Column(name = "extra_fare", nullable = false)
    private int value;

    private Fare(int value) {
        validateValue(value);
        this.value = value;
    }

    protected Fare() {}

    public static Fare from(int value) {
        return new Fare(value);
    }

    public static Fare empty() {
        return new Fare(0);
    }

    public void validateValue(int value) {
        if (value < MIN_VALUE) {
            throw new SubwayException(String.format("추가 요금은 %d원 보다 작을 수 없습니다.", MIN_VALUE));
        }
    }

    public int getValue() {
        return value;
    }

    public Fare add(Fare fare) {
        return new Fare(this.value + fare.value);
    }
}
