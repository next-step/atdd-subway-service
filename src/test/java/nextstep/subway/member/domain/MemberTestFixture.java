package nextstep.subway.member.domain;

public class MemberTestFixture {

    public static Member createMember(String email, String password, int age) {
        return Member.of(email, password, age);
    }
}
