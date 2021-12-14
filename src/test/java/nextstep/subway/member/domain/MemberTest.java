package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @DisplayName("사용자 패스워드를 잘못 입력하였을때 예외처리한다.")
    @Test
    void checkPassword() {
        final Member member = Member.of("email@email.com", "password", 20);

        assertThatThrownBy(() -> member.checkPassword("1234"))
                .isInstanceOf(AuthorizationException.class);
    }
}
