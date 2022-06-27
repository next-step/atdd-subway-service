package nextstep.subway.member.domain;

import nextstep.subway.member.exception.MemberException;
import nextstep.subway.member.exception.MemberExceptionType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Age {
    private static final int MIN_AGE = 0;
    @Column(name = "age", nullable = false)
    private int value;

    protected Age() {}

    private Age(final int age) {
        validation(age);
        this.value = age;
    }

    public static Age of(final int age) {
        return new Age(age);
    }

    public void validation(final int age) {
        if (age < MIN_AGE) {
            throw new MemberException(MemberExceptionType.INVALID_AGE);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Age{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Age age = (Age) o;
        return value == age.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
