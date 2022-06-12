package nextstep.subway.auth.domain;

import nextstep.subway.member.constant.MemberFarePolicy;

public interface LoginMember {
    Long getId();

    String getEmail();

    Integer getAge();

    MemberFarePolicy getMemberFarePolicy();
}
