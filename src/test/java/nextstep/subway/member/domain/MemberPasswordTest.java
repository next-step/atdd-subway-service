package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 비밀번호 도메인 관련")
class MemberPasswordTest {
    private MemberPassword memberPassword;

    @BeforeEach
    void setUp() {
        memberPassword = new MemberPassword("this is random password");
    }

    @DisplayName("회원 비밀번호를 확인할 수 있다")
    @Test
    void checkMemberPassword() {
        assertThatThrownBy(() -> {
            memberPassword.checkPassword("not correct password");

        }).isInstanceOf(AuthorizationException.class);
    }
}