package nextstep.subway.member.domain;

import nextstep.subway.fare.exception.BelowZeroIntegerException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Age {

    @Column(name = "age")
    private int value;

    protected Age() {

    }

    public Age(int value) {
        validateConstructor(value);
        this.value = value;
    }

    private void validateConstructor(int value) {
        if (value <= 0) {
            throw new BelowZeroIntegerException("나이는");
        }
    }

    public boolean isBetween(Age more, Age less) {
        if (more.value <= this.value && this.value < less.value) {
            return true;
        }

        return false;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Age age = (Age) o;
        return value == age.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
