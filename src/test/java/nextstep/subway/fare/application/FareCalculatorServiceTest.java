package nextstep.subway.fare.application;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorServiceTest {

    @ParameterizedTest(name = "요금 계산기 10키로 이하 기본 테스트")
    @CsvSource(value = {
            "1:1250", "4:1250", "6:1250", "10:1250",
    }, delimiter = ':')
    void calculateDefaultDistance(int distance, int resultFare) {
        int fare = FareCalculatorService.calculate(distance);
        assertThat(fare).isEqualTo(resultFare);
    }

    @ParameterizedTest(name = "요금 계산기 {0}키로 이하 성공 테스트")
    @CsvSource(value = {
            "11:1350", "15:1350", "16:1450", "19:1450",
            "20:1450", "21:1550", "26:1650", "29:1650",
            "30:1650", "31:1750", "40:1850", "49:2050", "50:2050"
    }, delimiter = ':')
    void calculate2(int distance, int resultFare) {
        int fare = FareCalculatorService.calculate(distance);
        assertThat(fare).isEqualTo(resultFare);
    }

    @ParameterizedTest(name = "요금 계산기 {0}키로 이상 성공 테스트")
    @CsvSource(value = {
            "58:2150", "66:2250", "74:2350", "82:2450",
            "130:3050", "146:3250", "154:3350", "178:3650",
    }, delimiter = ':')
    void calculateMaxDistance(int distance, int resultFare) {
        int fare = FareCalculatorService.calculate(distance);
        assertThat(fare).isEqualTo(resultFare);
    }
}
