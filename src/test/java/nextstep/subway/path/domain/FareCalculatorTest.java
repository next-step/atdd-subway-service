package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("거리가 10km 이하면 기본요금이다.")
    @ParameterizedTest
    @ValueSource(doubles = {1,2,9,10})
    void defaultFare(double distance) {
        assertThat(FareCalculator.calculate(distance)).isEqualTo(Fare.getDefaultFare());
    }

    // 요금이 바뀌면 테스트가 깨진다.
    @DisplayName("거리가 10~50km면 5km당 추가요금 발생한다")
    @ParameterizedTest
    @CsvSource(value = {"11:1250","12:1250", "15:1350", "20:1450", "50:2050"}, delimiter = ':')
    void moreTenlessFifty(double distance, int expected) {
        int fare = FareCalculator.calculate(distance).getFare();
        System.out.println(fare);
        assertThat(FareCalculator.calculate(distance)).isEqualTo(new Fare(expected));
    }

    // 요금이 바뀌면 테스트가 깨진다.
    @DisplayName("거리가 50km 초과하면 8km당 추가요금 발생한다")
    @ParameterizedTest
    @CsvSource(value = {"50:2050", "55:2050", "58:2150", "66:2250"}, delimiter = ':')
    void moreFifty(double distance, int expected) {
        System.out.println(FareCalculator.calculate(distance).getFare());
        assertThat(FareCalculator.calculate(distance)).isEqualTo(new Fare(expected));
    }

}
