package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare {

    private int value;

    protected Fare() {

    }

    public Fare(final int value) {
        validateNegativeValue(value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private void validateNegativeValue(int value) {
        if(value < 0) {
            throw new IllegalArgumentException("0보다 작을 수 없습니다.");
        }
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
