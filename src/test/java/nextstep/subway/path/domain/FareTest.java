package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FareTest {

    /**
     *   | 거리 | 어른 | 청소년 | 어린이 |
     *   | --- | --- | --- | --- |
     *   | 10km까지 | 1,250 | 720 | 450 |
     *   | 15km까지 | 1,350 | 800 | 500 |
     *   | 20km까지 | 1,450 | 880 | 550 |
     *   | 25km까지 | 1,550 | 960 | 600 |
     *   | 30km까지 | 1750 | 1,120 | 650 |
     */

    @ParameterizedTest
    @DisplayName("요금 연령별&거리별 계산")
    @CsvSource(value = {
            "10,25,600.0",
            "16,25,960.0",
            "21,25,1550",
            "10,10,450.0",
            "16,10,720.0",
            "21,10,1250.0",
            "25,20,1450",
            "7,30,650.0",
            "21,170,3550",
            "21,58,2150",
            "16,58,1440",
            "6,114,1250"
    })
    void calculate(int age, int distance, double fare) {
        Fare actual = new Fare(0,age, distance);
        BigDecimal expected = BigDecimal.valueOf(fare);
        assertThat(actual.getFare()).isEqualTo(expected);
    }

    @DisplayName("거리가 0보다 작은 경우")
    @Test
    void validateDistanceLessThanZero() {
        assertThatThrownBy(() -> {
            new Fare(0,10, -1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("나이가 0보다 작은 경우")
    @Test
    void validateAgeLessThanZero() {
        assertThatThrownBy(() -> {
            new Fare(0,-1, 10);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
