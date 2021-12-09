package nextstep.subway.path.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SubwayDistanceFeeTest {

    @DisplayName("거리에 따른 이용요금을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "9,1250", "11,1250", "49,1950", "57,2050", "58,2150", "120,2850"})
    void calculateSubwayUsageFee(int distance, int expected) {
        int fee = SubwayDistanceFee.calculateSubwayDistanceFee(distance);
        assertEquals(expected, fee);
    }
}
