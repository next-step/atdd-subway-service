package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @Test
    void create_기본요금에서_추가요금을_더한다() {
        assertThat(Fare.extra(700, 0, 20)).isEqualTo(Fare.of(1950));
    }

    @ParameterizedTest
    @MethodSource("거리별_추가요금")
    void create_거리별_추가요금을_더한다(int distance, int totalFare) {
        assertThat(Fare.extra(0, distance, 20)).isEqualTo(Fare.of(totalFare));
    }

    @ParameterizedTest
    @MethodSource("연령별_기본요금")
    void create_연령별_기본요금을_계산한다(int age, int defaultFare) {
        assertThat(Fare.extra(0, 0, age)).isEqualTo(Fare.of(defaultFare));
    }

    private static Stream<Arguments> 거리별_추가요금() {
        return Stream.of(
                Arguments.of(10, 1250),
                Arguments.of(11, 1350),
                Arguments.of(15, 1350),
                Arguments.of(16, 1450),
                Arguments.of(50, 2050),
                Arguments.of(51, 2150),
                Arguments.of(58, 2150),
                Arguments.of(59, 2250));
    }

    private static Stream<Arguments> 연령별_기본요금() {
        return Stream.of(
                Arguments.of(1, 0),
                Arguments.of(6, 450),
                Arguments.of(12, 450),
                Arguments.of(13, 720),
                Arguments.of(18, 720),
                Arguments.of(19, 1250),
                Arguments.of(30, 1250));
    }
}