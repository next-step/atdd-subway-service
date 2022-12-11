package nextstep.subway.member.domain;

import nextstep.subway.enums.ErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Age {
    private static final int MIN_AGE = 1;

    @Column(nullable = false)
    private Integer age;

    protected Age() {}

    private Age(Integer age) {
        validateEmpty(age);
        validateRange(age);
        this.age = age;
    }

    private void validateEmpty(Integer age) {
        if (Objects.isNull(age)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_BLANK.getMessage());
        }
    }

    private void validateRange(Integer age) {
        if (age < MIN_AGE) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_AGE_RANGE.getMessage());
        }
    }

    public static Age from(Integer age) {
        return new Age(age);
    }

    public int value() {
        return this.age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Age age1 = (Age) o;
        return age == age1.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }
}
