package nextstep.subway.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

/**
 * LoginMember 클래스 기능 검증
 */
@DisplayName("LoginMember 클래스 기능 검증")
class LoginMemberTest {

    @Test
    @DisplayName("로그인하지 않은 사용자 확인")
    void isNotLoginMember() {
        // given
        LoginMember loginMember = new LoginMember();

        // then
        assertThat(loginMember.isLoginUser()).isFalse();
    }

    @Test
    @DisplayName("로그인한 사용자 확인")
    void isLoginMember() {
        // given
        LoginMember loginMember = new LoginMember(1L, "g@gmail.com", 25);

        // then
        assertThat(loginMember.isLoginUser()).isTrue();
    }

    @TestFactory
    @DisplayName("로그인 사용자 연령 확인")
    List<DynamicTest> name() {
        return Arrays.asList(
                dynamicTest("영유아 확인", () -> {
                    // when
                    LoginMember loginMember = new LoginMember(1L, "g@gmail.com", 5);

                    // then
                    assertThat(loginMember.isBaby()).isTrue();
                }),
                dynamicTest("어린이 확인", () -> {
                    // when
                    LoginMember loginMember = new LoginMember(1L, "g@gmail.com", 12);

                    // then
                    assertThat(loginMember.isChild()).isTrue();
                }),
                dynamicTest("청소년 확인", () -> {
                    // when
                    LoginMember loginMember = new LoginMember(1L, "g@gmail.com", 18);

                    // then
                    assertThat(loginMember.isTeenager()).isTrue();
                }),
                dynamicTest("성인 확인", () -> {
                    // when
                    LoginMember loginMember = new LoginMember(1L, "g@gmail.com", 19);

                    // then
                    assertThat(loginMember.isAdult()).isTrue();
                })
        );
    }
}
