package nextstep.subway.line.dto;

import nextstep.subway.exception.InvalidDistanceRangeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTypeTest {
    private static Stream<Arguments> calculatorDistanceParams() {
        return Stream.of(
                Arguments.of(1, DistanceType.DEFAULT),
                Arguments.of(10, DistanceType.DEFAULT),
                Arguments.of(11, DistanceType.TEN_OVER),
                Arguments.of(50, DistanceType.TEN_OVER),
                Arguments.of(51, DistanceType.FIFTY_OVER)
        );
    }

    private static Stream<Arguments> calculatorAdditionalFareParams() {
        return Stream.of(
                Arguments.of(1, 0),
                Arguments.of(10, 0),
                Arguments.of(11, 100),
                Arguments.of(50, 800),
                Arguments.of(51, 900),
                Arguments.of(60, 1_000)
        );
    }

    @DisplayName("거리별 타입 계산")
    @ParameterizedTest
    @MethodSource("calculatorDistanceParams")
    void calculatorDistanceType(int distance, DistanceType distanceType) {
        DistanceType actual = DistanceType.calculatorDistanceType(distance);

        assertThat(actual).isEqualTo(distanceType);
    }

    @DisplayName("잘못된 거리를 입력했을 경우")
    @ParameterizedTest
    @ValueSource(ints = {
            -1, 0
    })
    void invalidDistanceTest(int distance) {
        assertThatThrownBy(() -> DistanceType.calculatorDistanceType(distance))
                .isInstanceOf(InvalidDistanceRangeException.class);
    }

    @DisplayName("거리별 추가운임 계산")
    @ParameterizedTest
    @MethodSource("calculatorAdditionalFareParams")
    void calculatorAdditionalFareTest(int distance, int fare) {
        DistanceType distanceType = DistanceType.calculatorDistanceType(distance);

        int actual = distanceType.calculatorAdditionalFare(distance);

        assertThat(actual).isEqualTo(fare);
    }
}