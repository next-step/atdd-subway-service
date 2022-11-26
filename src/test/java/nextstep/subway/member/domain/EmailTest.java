package nextstep.subway.member.domain;

import nextstep.subway.member.exception.MemberExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("사용자 이메일 관리 클래스 테스트")
class EmailTest {

    @ParameterizedTest
    @NullAndEmptySource
    void Email_객체_생성할때_null이거나_빈_문자열이_입력되면_IllegalArgumentException_발생(String email) {
        assertThatThrownBy(() -> {
            new Email(email);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberExceptionCode.REQUIRED_EMAIL.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "test", "testuser@", "testuser@test", "testuser#!@test.com", "testuser@test#.com" })
    void Email_객체_생성할때_잘못된_이메일_형식의_문자열이_입력되면_IllegalArgumentException_발생(String email) {
        assertThatThrownBy(() -> {
            new Email(email);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MemberExceptionCode.NOT_EMAIL_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "testuser@test.com", "test_user12@test.com", "test-user12@test.co.kr"})
    void Email_객체_생성(String email) {
        assertEquals(email, new Email(email).getEmail());
    }
}
