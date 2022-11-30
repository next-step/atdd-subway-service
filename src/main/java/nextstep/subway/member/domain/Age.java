package nextstep.subway.member.domain;

import nextstep.subway.common.exception.InvalidParameterException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Age {
    private static final String ERROR_MESSAGE_NULL_AGE = "나이를 입력해주세요.";
    private static final String ERROR_MESSAGE_INVALID_AGE_CRITERION = "나이는 0보다 커야합니다.";
    private static final int INVALID_AGE_CRITERION = 0;

    @Column(nullable = false)
    private Integer age;

    protected Age() {}

    private Age(Integer age) {
        validAge(age);
        this.age = age;
    }

    private void validAge(Integer age) {
        validNull(age);
        validAgeCriterion(age);
    }

    public static Age from(Integer age) {
        return new Age(age);
    }

    private static void validAgeCriterion(Integer age) {
        if (age <= INVALID_AGE_CRITERION) {
            throw new InvalidParameterException(ERROR_MESSAGE_INVALID_AGE_CRITERION);
        }
    }

    private static void validNull(Integer age) {
        if (Objects.isNull(age)) {
            throw new InvalidParameterException(ERROR_MESSAGE_NULL_AGE);
        }
    }

    public Integer value() {
        return age;
    }
}
