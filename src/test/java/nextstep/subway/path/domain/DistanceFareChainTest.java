package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFareChainTest {

    @ParameterizedTest
    @MethodSource("거리별_추가요금")
    void calculate(int distance, int overFare) {
        DistanceFareChain chain = new Distance10FareChain(new Distance50FareChain(new DistanceDefaultFareChain(null)));
        assertThat(chain.calculate(distance)).isEqualTo(overFare);
    }

    private static Stream<Arguments> 거리별_추가요금() {
        return Stream.of(
                Arguments.of(10, 0),
                Arguments.of(11, 100),
                Arguments.of(15, 100),
                Arguments.of(16, 200),
                Arguments.of(50, 800),
                Arguments.of(51, 900),
                Arguments.of(58, 900),
                Arguments.of(59, 1000));
    }
}