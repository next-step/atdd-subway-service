package nextstep.subway.line.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import nextstep.subway.line.application.policy.SubwayDistanceFarePolicy;
import nextstep.subway.line.domain.fare.Money;
import nextstep.subway.line.infrastructure.policy.DistanceFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFarePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {
        "10:1250",
        "11:1350",
        "15:1350",
        "16:1450",
        "50:2050",
        "51:2150",
        "59:2250",
    }, delimiter = ':')
    @DisplayName("거리요금 책정")
    void defaultDistanceFarePolicy(Integer distance, BigDecimal expected) {
        // given
        SubwayDistanceFarePolicy subwayFarePolicy = new DistanceFarePolicy();

        //when
        Money fare = subwayFarePolicy.calculateFare(distance);

        // then
        assertThat(fare.getMoney()).isEqualTo(expected);
    }

}
