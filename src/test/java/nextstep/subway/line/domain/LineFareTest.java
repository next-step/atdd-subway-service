package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.utils.Message.INVALID_OVER_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineFareTest {

    @DisplayName("추가요금이 0미만일 때 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {-1, -10})
    void exception(int input) {
        Assertions.assertThatThrownBy(() -> LineFare.from(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_OVER_FARE);
    }

    @DisplayName("추가요금이 0이상일 때 정상적으로 객체가 생성된다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {0, 10})
    void create(int input) {
        LineFare lineFare = LineFare.from(input);

        assertThat(lineFare).isNotNull();
    }

    @DisplayName("추가요금이 같은 LineFare 객체는 동등하다.")
    @Test
    void equals() {
        LineFare fare1 = LineFare.from(10);
        LineFare fare2 = LineFare.from(10);

        assertThat(fare1).isEqualTo(fare2);
    }

    @DisplayName("추가요금이 다른 LineFare 객체는 동등하지 않다.")
    @Test
    void notEquals() {
        LineFare fare1 = LineFare.from(10);
        LineFare fare2 = LineFare.from(20);

        assertThat(fare1).isNotEqualTo(fare2);
    }

    @DisplayName("LineFare 객체를 추가요금을 가지고 비교한다.")
    @Test
    void compare() {
        LineFare fare1 = LineFare.from(20);
        LineFare fare2 = LineFare.from(10);
        LineFare fare3 = LineFare.from(10);

        assertAll(
                () -> assertThat(fare1.compareTo(fare2) > 0).isTrue(),
                () -> assertThat(fare2.compareTo(fare1) < 0).isTrue(),
                () -> assertThat(fare2.compareTo(fare3) == 0).isTrue()
        );
    }
}
