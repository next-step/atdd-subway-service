package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest(name = "거리 : {0}KM, 노선추가요금 : {1}원, 나이 : {2}세일 때, 요금은 {3}원 이다.")
    @DisplayName("거리, 나이, 노선에 따른 요금을 계산한다.")
    @CsvSource(value = {"5:0:5:0", "5:0:8:450", "5:0:15:720", "5:0:25:1250", "5:0:70:0",
                        "30:0:5:0", "30:0:8:650", "30:0:15:1040", "30:0:25:1650", "30:0:70:0",
                        "60:0:5:0", "60:0:8:950", "60:0:15:1520", "60:0:25:2250", "60:0:70:0",
                        "60:1000:5:0", "60:1000:8:1950", "60:1000:15:2520", "60:1000:25:3250", "60:1000:70:0"}, delimiter = ':')
    void createFare(int distance, int additionalLineFare, int age, int expectedFare) {
        Fare fare = new Fare(distance, additionalLineFare, age);
        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

}
