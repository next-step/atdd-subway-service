package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest(name = "추가요금 {2}원이 부과되는 {0}km 구간의 요금은 {1}원이다")
    @CsvSource(value = {"10:2250:1000", "50:2550:500", "58:2150:0", "100:2750:0"}, delimiter = ':')
    void calculateFare(int distance, int expected, int extraFare) {
        // given & when
        int actual = Fare.calculateFare(distance, extraFare);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
