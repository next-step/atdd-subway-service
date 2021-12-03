package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFareChainTest {

    @ParameterizedTest
    @MethodSource("연령별_기본요금")
    void calculate_나이별_기본요금을_계산한다(int age, int fare) {
        AgeFareChain chain = new AgeChildFareChain(new AgeYouthFareChain(new AgeAdultFareChain(new AgeDefaultFareChain(null))));
        assertThat(chain.calculate(age)).isEqualTo(fare);
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