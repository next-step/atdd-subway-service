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
        assertThat(Fare.of(700).getValue()).isEqualTo(1950);
    }

    @ParameterizedTest
    @MethodSource("거리별_추가요금")
    void create_거리별_추가요금을_더한다(int distance, int totalFare) {
        Fare fare = Fare.of(0, distance);
        assertThat(fare.getValue()).isEqualTo(totalFare);
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
}