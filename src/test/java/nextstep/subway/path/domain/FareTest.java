package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("요금 객체의 Default_운임(기본요금)은 1250원이다.")
    @Test
    void createFare() {
        Fare fare = Fare.from();
        assertThat(fare.currentFare()).isEqualTo(1250);
    }

    @DisplayName("거리비례제를 적용하여 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "59:2250"}, delimiter = ':')
    void calculateFareByDistanceProportional(int distance, long expectFare) {
        Fare fare = Fare.from();
        fare.calculateFareByDistanceProportional(distance);
        assertThat(fare.currentFare()).isEqualTo(expectFare);
    }


}
