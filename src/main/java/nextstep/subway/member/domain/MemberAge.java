package nextstep.subway.member.domain;

import javax.persistence.Embeddable;

@Embeddable
public class MemberAge {
    private Integer age;

    protected MemberAge() {
    }

    public MemberAge(Integer age) {
        this.age = age;
    }

    public Integer findAge() {
        return age;
    }
}
