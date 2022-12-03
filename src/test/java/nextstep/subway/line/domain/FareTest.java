package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    @Test
    @DisplayName("요금을 받아 생성")
    void create() {
        // given
        int extraFare = 1_250;

        // when
        Fare fare = Fare.from(extraFare);

        // then
        assertThat(fare).isNotNull();
        assertThat(fare.getValue()).isEqualTo(extraFare);
    }

    @Test
    @DisplayName("20의 Fare에 10의 Fare를 더하면 30의 Fare가 생성된다.")
    void add() {
        // given
        Fare fare_20 = Fare.from(20);
        Fare fare_10 = Fare.from(10);

        // when
        Fare fare_30 = fare_20.add(fare_10);

        // then
        assertThat(fare_30.getValue()).isEqualTo(30);
    }

    @Test
    @DisplayName("30의 Fare에 10의 Fare를 빼면 20의 Fare가 생성된다.")
    void subtract() {
        // given
        Fare fare_30 = Fare.from(30);
        Fare fare_10 = Fare.from(10);

        // when
        Fare fare_20 = fare_30.subtract(fare_10);

        // then
        assertThat(fare_20.getValue()).isEqualTo(20);
    }
}
