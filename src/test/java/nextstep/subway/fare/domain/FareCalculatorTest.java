package nextstep.subway.fare.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.fare.domain.FareConstant.BASIC_FARE;

class FareCalculatorTest {

    @DisplayName("거리가 10km 이내일 때 기본운임은 1,250원이다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {0, 5, 10})
    void basicFare(int input) {
        FareCalculator fareCalculator = new SubwayFareCalculator();

        int fare = fareCalculator.calculate(input);

        Assertions.assertThat(fare).isEqualTo(BASIC_FARE);
    }
}
