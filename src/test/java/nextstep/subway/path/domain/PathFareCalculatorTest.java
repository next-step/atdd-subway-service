package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("거리 비례 금액 계산 클래스 테스트")
class PathFareCalculatorTest {

    @DisplayName("기본 운임 계산")
    @Test
    void calculateDefaultFare() {
        assertEquals(1250, PathFareCalculator.calculateDefaultFare());
    }

    @DisplayName("중거리 운임 계산")
    @ParameterizedTest
    @CsvSource(value = { "15:1350", "20:1450", "30:1650", "40:1850", "50:2050" }, delimiter = ':')
    void calculateMiddleFare(int distance, int expected) {
        assertEquals(expected, PathFareCalculator.calculateMiddleFare(distance));
    }

    @DisplayName("장거리 운임 계산")
    @ParameterizedTest
    @CsvSource(value = { "58:2150", "74:2350", "90:2550", "122:2950", "170:3550" }, delimiter = ':')
    void calculateLongFare(int distance, int expected) {
        assertEquals(expected, PathFareCalculator.calculateLongFare(distance));
    }
}
