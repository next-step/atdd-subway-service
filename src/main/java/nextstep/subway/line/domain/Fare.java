package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.exception.InvalidArgumentException;

@Embeddable
public class Fare {
    private static final int FARE_MIN_NUMBER = 0;
    private Integer fare = FARE_MIN_NUMBER;

    protected Fare() {
    }

    private Fare(Integer fare) {
        validate(fare);
        this.fare = fare;
    }

    public static Fare valueOf(Integer fare) {
        return new Fare(fare);
    }

    public Integer get() {
        return fare;
    }

    private void validate(Integer fare) {
        if (fare < FARE_MIN_NUMBER) {
            throw new InvalidArgumentException(String.format("%s 이상의 정수만 입력가능합니다.", FARE_MIN_NUMBER));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare other = (Fare) o;
        return fare.equals(other.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
