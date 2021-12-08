package nextstep.subway.member.fixture;

import nextstep.subway.member.domain.Member;

public class MemberFixture {
    public static Member 사용자1() {
        return new Member("user1@gmail.com","password",10);
    }

    public static Member 사용자2() {
        return new Member("user2@gmail.com","password",20);
    }
}
