package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.MemberShip;

public class GuestMember extends LoginMember {
    private Long id;
    private String email;
    private final Integer age;

    public GuestMember() {
        this.age = MemberShip.GENERAL.getMinAge();
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
