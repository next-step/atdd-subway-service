package nextstep.subway.member.domain;


import nextstep.subway.exception.AuthorizationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.utils.Message.WRONG_PASSWORD;

class MemberTest {

    @DisplayName("두개의 비밀번호가 다르면 예외가 발생한다.")
    @Test
    void checkPassword() {
        Member member = new Member("yalmung@gmail.com", "password", 10);

        Assertions.assertThatThrownBy(() -> member.checkPassword("wrong password"))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith(WRONG_PASSWORD);
    }

}
