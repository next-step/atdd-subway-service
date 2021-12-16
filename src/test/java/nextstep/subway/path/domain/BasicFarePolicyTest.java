package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;

class BasicFarePolicyTest {

    @DisplayName("기본 요금 계산")
    @Test
    void calculateFare() {
        BasicFarePolicy farePolicy = new BasicFarePolicy();
        Distance distance = new Distance(10);

        assertThat(farePolicy.calculateFare(distance)).isEqualTo(Fare.from(1250));
    }
}