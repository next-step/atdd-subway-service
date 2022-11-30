package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.exception.MemberExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("사용자 비밀번호 관리 클래스 테스트")
class PasswordTest {

    @ParameterizedTest
    @NullAndEmptySource
    void Password_객체_생성할때_null이거나_빈_문자열이_입력되면_IllegalArgumentException_발생(String password) {
        assertThatThrownBy(() -> {
            new Password(password);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberExceptionCode.REQUIRED_PASSWORD.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "passwd", "password(157)", "longpasswordtest1570", "<<password>>" })
    void Password_객체_생성할때_잘못된_비밀번호_형식의_문자열이_입력되면_IllegalArgumentException_발생(String password) {
        assertThatThrownBy(() -> {
            new Password(password);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberExceptionCode.NOT_PASSWORD_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "password15#$", "Password^7&J!", "TeStPaSsWord@%!" })
    void Password_객체_생성(String password) {
        assertThat(new Password(password)).isNotNull();
    }

    @Test
    void 입력된_비밀번호가_사용자의_비밀번호와_일치하지_않으면_AuthorizationException_발생() {
        Password password = new Password("password157#");

        assertThatThrownBy(() -> {
            password.checkPassword("password369#");
        }).isInstanceOf(AuthorizationException.class)
                .hasMessage(MemberExceptionCode.PASSWORD_NOT_MATCH.getMessage());
    }
}
