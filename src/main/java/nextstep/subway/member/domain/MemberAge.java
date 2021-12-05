package nextstep.subway.member.domain;

import javax.persistence.Embeddable;

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
}
