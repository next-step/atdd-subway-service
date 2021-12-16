package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BasicFarePolicyTest {

    @DisplayName("기본 요금 계산")
    @Test
    void calculateFare() {
        BasicFarePolicy farePolicy = new BasicFarePolicy();

        assertThat(farePolicy.calculateFare(1)).isEqualTo(Fare.from(1250));
    }
}