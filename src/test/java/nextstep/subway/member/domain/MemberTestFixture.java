package nextstep.subway.member.domain;

public class MemberTestFixture {
    public static Member member(String email, String password, int age) {
        return Member.of(email, password, age);
    }

    public static Member member(long id, String email, String password, int age) {
        return Member.of(id, email, password, age);
    }
}
