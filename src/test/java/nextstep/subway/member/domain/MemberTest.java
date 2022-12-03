package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.common.exception.ErrorEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberTest {
    @Test
    void 비밀번호가_일치하지_않으면_예외_발생() {
        Member member = new Member("abc@gmail.com", "password", 10);

        Assertions.assertThatThrownBy(() -> member.checkPassword("wrong password"))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_MATCH_PASSWORD.message());
    }
}
