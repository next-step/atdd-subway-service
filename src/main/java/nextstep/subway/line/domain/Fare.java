package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Fare implements Comparable<Fare> {

    public static final Fare ZERO = new Fare(0);

    private Integer value;

    public Fare(Integer value) {
        this.value = validate(value);
    }

    private Integer validate(Integer value) {
        requireNonNull(value, "value");
        if (value < 0) {
            throw new InvalidFareException("유효하지 않은 요금입니다.");
        }
        return value;
    }

    protected Fare() {
    }

    public Fare plus(Fare fare) {
        requireNonNull(fare, "fare");
        return new Fare(value + fare.value);
    }

    public Fare minus(Fare fare) {
        requireNonNull(fare, "fare");
        if (value < fare.value) {
            throw new InvalidFareException("차감하려는 요금이 더 클 수 없습니다.");
        }
        return new Fare(value - fare.value);
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public int compareTo(Fare other) {
        return Integer.compare(value, other.value);
    }
}
