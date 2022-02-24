package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @DisplayName("거리에 알맞은 금액을 리턴한다.")
    @Test
    void calculate_over_fare() {
        int distance = 60;
        Fare fare = Fare.of(distance);
        assertThat(fare.getFare()).isEqualTo(1950);
    }
}