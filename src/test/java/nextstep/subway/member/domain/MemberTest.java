package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.common.ErrorCode;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberTest {

    @DisplayName("비밀번호가 다르면 예외 발생")
    void makeExceptionWhenPasswordIsNotMatch() {
        Member member = new Member("abc@gmail.com", "password", 10);

        assertThatThrownBy(() -> member.checkPassword("wrong password"))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith(ErrorCode.INVALID_PASSWORD.getErrorMessage());
    }
}
