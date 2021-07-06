package nextstep.subway.path.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FareOfDistancePolicyFactoryTest {

    @ParameterizedTest(name = "거리에 따른 요금 계산, distance={0}, expectedTotalFare={1}")
    @MethodSource
    void calculateTest(int distance, int expectedTotalFare) {
        // when
        int totalFare = FareOfDistancePolicyFactory.calculate(distance);

        // then
        assertThat(totalFare).isEqualTo(expectedTotalFare);
    }

    private static Stream<Arguments> calculateTest() {
        return Stream.of(
            Arguments.of(1, 1250),
            Arguments.of(9, 1250),
            Arguments.of(10, 1250),
            Arguments.of(11, 1350),
            Arguments.of(50, 2050),
            Arguments.of(52, 2150),
            Arguments.of(59, 2250)
                        );
    }

}
