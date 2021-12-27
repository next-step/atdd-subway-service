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
                () -> assertThat(FarePolicy.calculateDistanceOverFare(58)).isEqualTo(900),
                () -> assertThat(FarePolicy.calculateDistanceOverFare(45)).isEqualTo(700)
        );
    }

    @DisplayName("연령별 요금 계산")
    @Test
    void discountFare() {
        assertAll(
                () -> assertThat(FarePolicy.calculateDiscountAgeFare(13, 1_250)).isEqualTo(720),
                () -> assertThat(FarePolicy.calculateDiscountAgeFare(12, 1_250)).isEqualTo(450),
                () -> assertThat(FarePolicy.calculateDiscountAgeFare(null, 1_250)).isEqualTo(1_250),
                () -> assertThat(FarePolicy.calculateDiscountAgeFare(20, 1_250)).isEqualTo(1_250)
        );
    }
}
