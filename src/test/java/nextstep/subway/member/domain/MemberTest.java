package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;
import nextstep.subway.common.exception.AuthorizationException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MemberTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Member.of(
                Email.from("email@email.com"), Password.from("password"), Age.from(1)));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 객체화 불가능")
    @DisplayName("이메일, 패스워드, 나이는 필수")
    @MethodSource
    void instance_nullEmailPasswordAge_thrownIllegalArgumentException(
        Email email, Password password, Age age) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Member.of(email, password, age))
            .withMessageEndingWith("필수입니다.");
    }

    @Test
    @DisplayName("수정")
    void update() {
        // given
        Member member = Member.of(
            Email.from("email@email.com"), Password.from("password"), Age.from(1));
        Member newMember = Member.of(
            Email.from("newEmail@email.com"), Password.from("newPassword"), Age.from(2));

        // when
        member.update(newMember);

        // then
        assertThat(member).isEqualTo(newMember);
    }

    @Test
    @DisplayName("패스워드 체크")
    void checkPassword() {
        // given
        Member member = Member.of(
            Email.from("email@email.com"), Password.from("password"), Age.from(1));

        // when
        ThrowingCallable checkPasswordCallable = () ->
            member.checkPassword(Password.from("invalid"));

        // then
        assertThatExceptionOfType(AuthorizationException.class)
            .isThrownBy(checkPasswordCallable);
    }

    private static Stream<Arguments> instance_nullEmailPasswordAge_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(Email.from("email@email.com"), Password.from("password"), null),
            Arguments.of(Email.from("email@email.com"), null, Age.from(1)),
            Arguments.of(null, Password.from("password"), Age.from(1))
        );
    }
}
