package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFarePolicyTest {

    private DistanceFarePolicy distanceFarePolicy;

    @BeforeEach
    void setUp() {
        distanceFarePolicy = new DistanceFarePolicy();
    }

    @Test
    void 기본_요금_계산() {
        assertThat(distanceFarePolicy.calculate(Distance.from(10))).isEqualTo(Fare.from(1250));
    }

    @ParameterizedTest
    @CsvSource({
            "11, 1350",
            "30, 1650",
            "50, 2050"
    })
    void 십키로_초과_오십키로_이하_요금_계산(int distance, double fare) {

        assertThat(distanceFarePolicy.calculate(Distance.from(distance))).isEqualTo(Fare.from(fare));
    }

    @ParameterizedTest
    @CsvSource({
            "58, 2150",
            "66, 2250",
    })
    void 오십키로_초과_요금_계산(int distance, double fare) {
        assertThat(distanceFarePolicy.calculate(Distance.from(distance))).isEqualTo(Fare.from(fare));
    }
}