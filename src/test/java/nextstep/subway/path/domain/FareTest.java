package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FareTest {
    @DisplayName("거리 정책에 따른 요금 산정")
    @Test
    void calculateChargeByDistance() {
        assertAll(() -> {
           assertThat(Fare.of(5).getFare()).isEqualTo(1250);
           assertThat(Fare.of(10).getFare()).isEqualTo(1250);
           assertThat(Fare.of(25).getFare()).isEqualTo(1550);
           assertThat(Fare.of(27).getFare()).isEqualTo(1550);
           assertThat(Fare.of(57).getFare()).isEqualTo(2050);
           assertThat(Fare.of(58).getFare()).isEqualTo(2150);
        });
    }
}
