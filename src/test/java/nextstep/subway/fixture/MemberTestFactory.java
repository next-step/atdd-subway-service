package nextstep.subway.fixture;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class MemberTestFactory {
    private MemberTestFactory() {}

    public static Member create(String email, String password, int age) {
        return new Member(email, password, age);
    }

    public static LoginMember createLoginMember(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
