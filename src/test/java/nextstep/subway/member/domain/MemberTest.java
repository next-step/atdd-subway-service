package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.message.ExceptionMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("두개의 비밀번호가 다르면 예외를 발생한다.")
    @Test
    void checkPassword() {
        Member member = new Member("abc@gmail.com", "password", 10);

        Assertions.assertThatThrownBy(() -> member.checkPassword("wrong password"))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_PASSWORD);
    }
}
