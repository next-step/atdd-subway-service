package nextstep.subway.member.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {
    public static Member 회원(Long id, String email, String password, int age) {
        Member member = new Member(email, password, age);
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }
}
