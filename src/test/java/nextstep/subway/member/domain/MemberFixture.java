package nextstep.subway.member.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    private static Member member1 = new Member("email@email.com", "password", 20);
    private static Member member2 = new Member("email2@email.com", "qwerty1234", 32);

    public static Member 회원1() {
        ReflectionTestUtils.setField(member1, "id", 1L);
        return member1;
    }

    public static Member 회원2() {
        ReflectionTestUtils.setField(member2, "id", 2L);
        return member2;
    }
}
