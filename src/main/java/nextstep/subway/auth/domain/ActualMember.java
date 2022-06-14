package nextstep.subway.auth.domain;

import nextstep.subway.member.constant.MemberFarePolicy;

public class ActualMember implements AccessMember {
    private Long id;
    private String email;
    private Integer age;

    public ActualMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    @Override
    public MemberFarePolicy getMemberFarePolicy() {
        return MemberFarePolicy.convert(age);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Integer getAge() {
        return age;
    }
}
