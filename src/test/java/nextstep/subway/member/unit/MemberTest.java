package nextstep.subway.member.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {

    @DisplayName("사용자의 비밀번호와 다른 비밀번호를 입력받으면 AuthorizationException이 발생한다.")
    @Test
    void checkPassword_exception() {
        String email = "email@email.com";
        String password = "password";
        String wrongPassword = "wrongPassword";
        int age = 10;
        // given
        Member member = new Member(email, password ,age);
        // then
        assertThatThrownBy(() ->member.checkPassword(wrongPassword)).isInstanceOf(AuthorizationException.class);

    }

}
