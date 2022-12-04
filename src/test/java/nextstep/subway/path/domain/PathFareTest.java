package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("거리에 따른 지하철 운임 비용 계산 테스트")
class PathFareTest {

    @DisplayName("거리와 일치하는 PathFare를 반환")
    @ParameterizedTest
    @CsvSource(value = { "5:DEFAULT", "10:DEFAULT", "25:MIDDLE", "40:MIDDLE", "50:MIDDLE", "60:LONG",
            "80:LONG", "100:LONG" }, delimiter = ':')
    void match(int distance, PathFare expected) {
        assertEquals(expected, PathFare.match(distance));
    }

    @DisplayName("기본 운임 비용 계산")
    @Test
    void calculateDefaultFare() {
        assertEquals(1250, PathFare.match(10).getFare(10));
    }

    @DisplayName("중거리 운임 비용 계산")
    @ParameterizedTest
    @CsvSource(value = { "15:1350", "20:1450", "30:1650", "40:1850", "50:2050" }, delimiter = ':')
    void calculateMiddleFare(int distance, int expected) {
        assertEquals(expected, PathFare.match(distance).getFare(distance));
    }

    @DisplayName("장거리 운임 비용 계산")
    @ParameterizedTest
    @CsvSource(value = { "58:2150", "74:2350", "90:2550", "122:2950", "170:3550" }, delimiter = ':')
    void calculateLongFare(int distance, int expected) {
        assertEquals(expected, PathFare.match(distance).getFare(distance));
    }
}
