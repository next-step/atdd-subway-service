package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.exception.MemberExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("회원 클래스 테스트")
class MemberTest {

    @Test
    void 동등성_테스트() {
        assertEquals(new Member("testuser@test.com", "password157#", 20),
                new Member("testuser@test.com", "password157#", 20));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void Member_객체_생성할때_email이_null이거나_빈_문자열이_입력되면_IllegalArgumentException_발생(String email) {
        assertThatThrownBy(() -> {
            new Member(email, "password157#", 20);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberExceptionCode.REQUIRED_EMAIL.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "test", "testuser@", "testuser@test", "testuser#!@test.com", "testuser@test#.com" })
    void Member_객체_생성할때_잘못된_이메일_형식의_문자열이_입력되면_IllegalArgumentException_발생(String email) {
        assertThatThrownBy(() -> {
            new Member(email, "password157#", 20);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberExceptionCode.NOT_EMAIL_FORMAT.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void Member_객체_생성할때_password가_null이거나_빈_문자열이_입력되면_IllegalArgumentException_발생(String password) {
        assertThatThrownBy(() -> {
            new Member("testuser@test.com", password, 20);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberExceptionCode.REQUIRED_PASSWORD.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "passwd", "password(157)", "longpasswordtest1570", "<<password>>" })
    void Member_객체_생성할때_잘못된_비밀번호_형식의_문자열이_입력되면_IllegalArgumentException_발생(String password) {
        assertThatThrownBy(() -> {
            new Member("testuser@test.com", password, 20);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberExceptionCode.NOT_PASSWORD_FORMAT.getMessage());
    }

    @Test
    void Member_수정() {
        Member member = new Member("testuser@test.com", "password157#", 20);
        member.update(new Member("test-user@test.com", "password157#", 25));

        assertAll(
                () -> assertEquals("test-user@test.com", member.getEmail()),
                () -> assertEquals(25, member.getAge())
        );
    }

    @Test
    void 입력된_비밀번호가_사용자의_비밀번호와_일치하지_않으면_AuthorizationException_발생() {
        Member member = new Member("testuser@test.com", "password157#", 20);
        
        assertThatThrownBy(() -> {
            member.checkPassword("password369#");
        }).isInstanceOf(AuthorizationException.class)
                .hasMessage(MemberExceptionCode.PASSWORD_NOT_MATCH.getMessage());
    }
}
