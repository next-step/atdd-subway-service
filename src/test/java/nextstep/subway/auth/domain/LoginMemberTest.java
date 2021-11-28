package nextstep.subway.auth.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("로그인 사용자")
class LoginMemberTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> LoginMember.of(1, Email.from("email@email.com"), Age.from(1)));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 객체화 불가능")
    @DisplayName("이메일, 나이는 필수")
    @MethodSource
    void instance_nullEmailAge_thrownIllegalArgumentException(Email email, Age age) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> LoginMember.of(1, email, age))
            .withMessageEndingWith("필수입니다.");
    }

    private static Stream<Arguments> instance_nullEmailAge_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, Age.from(1)),
            Arguments.of(Email.from("email@email.com"), null)
        );
    }
}
