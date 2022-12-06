package nextstep.subway.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.exception.InvalidParameterException;

@Embeddable
public class Age {
    private static final String ERROR_MESSAGE_INVALID_AGE_CRITERION = "나이는 0보다 커야합니다.";
    private static final int INVALID_AGE_CRITERION = 0;

    @Column(nullable = false)
    private Integer age;

    protected Age() {}

    private Age(int age) {
        validAge(age);
        this.age = age;
    }

    private void validAge(int age) {
        validAgeCriterion(age);
    }

    public static Age from(int age) {
        return new Age(age);
    }

    private static void validAgeCriterion(Integer age) {
        if (age <= INVALID_AGE_CRITERION) {
            throw new InvalidParameterException(ERROR_MESSAGE_INVALID_AGE_CRITERION);
        }
    }

    public boolean between(int start, int end) {
        return start <= age && age <= end;
    }

    public Integer value() {
        return age;
    }
}
