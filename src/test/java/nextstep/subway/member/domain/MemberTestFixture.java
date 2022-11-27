package nextstep.subway.member.domain;

import nextstep.subway.auth.domain.LoginMember;

public class MemberTestFixture {

    public static Member createMember(String email, String password, int age) {
        return Member.of(email, password, age);
    }

    public static LoginMember createLoginMember(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
