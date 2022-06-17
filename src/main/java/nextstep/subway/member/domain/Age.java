package nextstep.subway.member.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Age {
    public static final int MIN_NUM = 0;
    @Column
    private int age;

    private Age(int age) {
        validateAge(age);
        this.age = age;
    }

    protected Age() {
    }

    public static Age valueOf(int age) {
        return new Age(age);
    }

    private void validateAge(int age) {
        if (age < MIN_NUM) {
            throw new IllegalArgumentException("음수는 유효하지 않습니다.");
        }
    }

    public int age() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Age age1 = (Age) o;
        return age == age1.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }
}
