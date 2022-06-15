package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DistanceFarePolicyTest {

    @DisplayName("거리에 따른 운임요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "10:1250",
            "50:2050",
            "51:2150"
    }, delimiter = ':')
    void testDistancePolicy(int distance, int fare) {
        assertThat(DistanceFarePolicy.calculateFare(distance)).isEqualTo(fare);
    }

}
