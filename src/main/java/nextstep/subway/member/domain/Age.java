package nextstep.subway.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.constant.ErrorCode;

@Embeddable
public class Age {

    private static final int ZERO = 0;

    @Column(nullable = false)
    private int age;

    protected Age() {}

    private Age(int age) {
        validateAge(age);
        this.age = age;
    }

    public static Age from(int age) {
        return new Age(age);
    }

    private void validateAge(int age) {
        if(age <= ZERO) {
            throw new IllegalArgumentException(ErrorCode.나이는_0보다_작거나_같을_수_없음.getErrorMessage());
        }
    }

    public int value() {
        return age;
    }
}
