package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareTest {

    @DisplayName("기본거리 운임")
    @CsvSource(value = {"1:1250", "5:1250", "10:1250"}, delimiter = ':')
    @ParameterizedTest
    void defaultFare(int distance, int totalFare) {
        Fare fare = new Fare(distance);
        assertThat(fare.getFare()).isEqualTo(totalFare);
    }

    @DisplayName("10~50키로 사이일때 추가운임")
    @CsvSource(value = {"11:1350", "15:1350", "16:1450"}, delimiter = ':')
    @ParameterizedTest
    void overFareBy5km(int distance, int totalFare) {
        Fare fare = new Fare(distance);
        assertThat(fare.getFare()).isEqualTo(totalFare);
    }

    @DisplayName("50키로 이상일때 추가운임")
    @CsvSource(value = {"51:2150", "59:2250", "67:2350"}, delimiter = ':')
    @ParameterizedTest
    void overFareBy8km(int distance, int totalFare) {
        Fare fare = new Fare(distance);
        assertThat(fare.getFare()).isEqualTo(totalFare);
    }

}
