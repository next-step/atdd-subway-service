package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceFareTest {

    @DisplayName("거리에 알맞은 추가금액을 리턴한다.")
    @Test
    void calculate_over_fare() {
        int distance = 60;
        DistanceFare distanceFare = DistanceFare.of(distance);
        assertThat(distanceFare.calculateOverFare(distance)).isEqualTo(700);
    }

}