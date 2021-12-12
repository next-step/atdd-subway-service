package nextstep.subway.member.domain;

import static nextstep.subway.member.step.MemberFixtures.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.subway.auth.application.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    private Member member;

    @BeforeEach
    public void setUp() {
        // given
        member = 회원생성();
    }

    @Test
    @DisplayName("비밀번호 불일치 실패")
    void checkPassword() {
        // then
        assertThrows(AuthorizationException.class, () -> member.checkPassword(NEW_PASSWORD));
    }

}
