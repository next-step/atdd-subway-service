package nextstep.subway.member.domain;

public class MemberTestFixture {
    public static Member member(String email, String password, int age) {
        return Member.of(email, password, age);
    }
}
