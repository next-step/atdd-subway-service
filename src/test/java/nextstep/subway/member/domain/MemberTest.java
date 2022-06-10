package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @DisplayName("회원 정보 수정 시 해당 내용으로 수정되어야 한다")
    @Test
    void memberUserUpdateTest() {
        // given
        Member 유저 = 유저_생성("email@test.com", "1234", 20);
        Member 업데이트_될_유저 = 유저_생성("change@test.com", "change", 20);

        // when
        유저.update(업데이트_될_유저);

        // then
        assertThat(유저.getEmail()).isEqualTo(업데이트_될_유저.getEmail());
        assertThat(유저.getPassword()).isEqualTo(업데이트_될_유저.getPassword());
        assertThat(유저.getAge()).isEqualTo(업데이트_될_유저.getAge());
    }

    @DisplayName("회원의 패스워드 확인 기능은 정상 동작해야 한다")
    @Test
    void memberUserCheckPasswordTest() {
        // given
        String 패스워드 = "1234";
        Member 유저 = 유저_생성("email@test.com", 패스워드, 20);

        // then
        assertThatNoException().isThrownBy(() -> 유저.checkPassword(패스워드));
        assertThatThrownBy(() -> 유저.checkPassword(패스워드 + "something")).isInstanceOf(AuthorizationException.class);
    }

    public static Member 유저_생성(String email, String password, int age) {
        return new Member(email, password, age);
    }
}
