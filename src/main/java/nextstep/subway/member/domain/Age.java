package nextstep.subway.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Age {
    private static final int MIN_AGE = 0;
    private static final String MIN_AGE_ERROR_MESSAGE = "나이는 최소 0보다 커야 합니다.";
    private static final int CHILD_MIN_AGE = 6;
    private static final int CHILD_MAX_AGE = 12;
    private static final int YOUTH_MIN_AGE = 13;
    private static final int YOUTH_MAX_AGE = 18;

    @Column
    private int age;

    protected Age() {
    }

    private Age(int age) {
        validate(age);
        this.age = age;
    }

    private void validate(int age) {
        if (age <= MIN_AGE) {
            throw new IllegalArgumentException(MIN_AGE_ERROR_MESSAGE);
        }
    }

    public static Age of(int age) {
        return new Age(age);
    }

    public boolean isChild() {
        return age >= CHILD_MIN_AGE && age <= CHILD_MAX_AGE;
    }

    public boolean isYouth() {
        return age >= YOUTH_MIN_AGE && age <= YOUTH_MAX_AGE;
    }

    public int getAge() {
        return age;
    }
}
