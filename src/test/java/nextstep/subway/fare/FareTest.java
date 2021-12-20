package nextstep.subway.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FareTest {
    @DisplayName("요금을 계산한다.")
    @Test
    void calculateFare() {
        assertAll(
                () -> assertThat(FarePolicy.calculateDistanceOverFare(58)).isEqualTo(2_150),
                () -> assertThat(FarePolicy.calculateDistanceOverFare(45)).isEqualTo(1_950)
        );
    }
}
