package nextstep.subway.auth.domain;

import nextstep.subway.member.constant.MemberFarePolicy;

public class AnonymousMember implements LoginMember {
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public Integer getAge() {
        return null;
    }

    @Override
    public MemberFarePolicy getMemberFarePolicy() {
        return null;
    }
}
