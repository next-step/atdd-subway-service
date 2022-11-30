package nextstep.subway.line.domain;

import nextstep.subway.exception.NegativeOverFareException;
import nextstep.subway.message.ExceptionMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LineFareTest {

    @DisplayName("추가요금이 0미만일 때 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {-1, -10})
    void exception(int input) {
        Assertions.assertThatThrownBy(() -> LineFare.from(input))
                .isInstanceOf(NegativeOverFareException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_OVER_FARE);
    }

    @DisplayName("추가요금이 0이상일 때 정상적으로 객체가 생성된다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {0, 10})
    void create(int input) {
        LineFare lineFare = LineFare.from(input);

        Assertions.assertThat(lineFare).isNotNull();
    }
}
