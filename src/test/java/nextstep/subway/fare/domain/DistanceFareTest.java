package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DistanceFareTest {
    @DisplayName("10km 인 경우 요금 계산")
    @Test
    void calculate_10km_fare() {
        // given & when
        int fare = DistanceFare.calculateDistanceFare(10);
        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("15km 인 경우 요금 계산")
    @Test
    void calculate_15km_fare() {
        // given & when
        int fare = DistanceFare.calculateDistanceFare(15);
        // then
        assertThat(fare).isEqualTo(1350);
    }

    @DisplayName("58km 인 경우 요금 계산")
    @Test
    void calculate_58km_fare() {
        // given & when
        int fare = DistanceFare.calculateDistanceFare(58);
        // then
        assertThat(fare).isEqualTo(2150);
    }
}