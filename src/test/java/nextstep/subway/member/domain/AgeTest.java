package nextstep.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

import static nextstep.subway.member.domain.Age.CHILD_END_AGE;
import static nextstep.subway.member.domain.Age.CHILD_START_AGE;
import static nextstep.subway.member.domain.Age.TEENAGER_END_AGE;
import static nextstep.subway.member.domain.Age.TEENAGER_START_AGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.ParameterizedTest.DEFAULT_DISPLAY_NAME;

@DisplayName("나이 테스트")
class AgeTest {

    @DisplayName("생성 성공")
    @Test
    void create_age_success() {
        final int source = 10;
        final Age age = Age.from(source);
        assertThat(age.getAge()).isEqualTo(source);
    }

    @ParameterizedTest(name = "어린이 테스트 " + DEFAULT_DISPLAY_NAME)
    @MethodSource(value = "childAge")
    void childCreate_age_success(int age) {
        assertThat(Age.from(age).isChild()).isTrue();
    }

    @ParameterizedTest(name = "청소년 테스트 " + DEFAULT_DISPLAY_NAME)
    @MethodSource(value = "teenagerAge")
    void teenagerCreate_age_success(int age) {
        assertThat(Age.from(age).isTeenager()).isTrue();
    }

    private static IntStream childAge() {
        return IntStream.range(CHILD_START_AGE, CHILD_END_AGE + 1);
    }

    private static IntStream teenagerAge() {
        return IntStream.range(TEENAGER_START_AGE, TEENAGER_END_AGE + 1);
    }
}
