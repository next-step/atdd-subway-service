package nextstep.subway.member.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MemberAge {
    private int age;

    protected MemberAge() {
    }

    public MemberAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAge memberAge = (MemberAge) o;
        return age == memberAge.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }
}
