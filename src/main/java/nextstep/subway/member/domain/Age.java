package nextstep.subway.member.domain;

import nextstep.subway.exception.NotValidDataException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static nextstep.subway.exception.type.ValidExceptionType.MIN_AGE_NOT_ZERO;

@Embeddable
public class Age {

    private static final int MIN_AGE = 0;

    @Column(nullable = false)
    private int age;

    protected Age() {
    }

    private Age(int age) {
        validCheckAgeIsNotMinus(age);
        this.age = age;
    }

    public static Age from(int age) {
        return new Age(age);
    }

    private void validCheckAgeIsNotMinus(int age) {
        if (age < MIN_AGE) {
            throw new NotValidDataException(MIN_AGE_NOT_ZERO.getMessage());
        }
    }

    public int getAge() {
        return age;
    }

    public AgeType getType() {
        return AgeType.getAgeType(age);
    }
}
