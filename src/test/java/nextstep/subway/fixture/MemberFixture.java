package nextstep.subway.fixture;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.domain.Member;

public class MemberFixture {

    public static Member 회원 = new Member("test@test.com", "test", 31);
    public static LoginMember 로그인된_어린이_회원 = new LoginMember(null, "test@test.com", 6);
    public static LoginMember 로그인된_청소년_회원 = new LoginMember(null, "test@test.com", 15);
    public static LoginMember 로그인된_일반_회원 = new LoginMember(null, "test@test.com", 20);
    public static LoginMember 익명 = LoginMember.anonymous();
}
