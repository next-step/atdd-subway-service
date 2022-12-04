package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {
    @Test
    @DisplayName("요금 생성")
    void createFare() {
        // when
        Fare actual = Fare.from(1250);

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertThat(actual).isInstanceOf(Fare.class)
        );
    }

    @Test
    @DisplayName("요금 더하기")
    void addFare() {
        // given
        Fare fare = Fare.from(1250);
        Fare fare2 = Fare.from(300);

        // when
        Fare actual = fare.add(fare2);

        // then
        assertThat(actual.value()).isEqualTo(1550);
    }

    @Test
    @DisplayName("요금 빼기")
    void subtractFare() {
        // given
        Fare fare = Fare.from(1250);
        Fare fare2 = Fare.from(350);

        // when
        Fare actual = fare.subtract(fare2);

        // then
        assertThat(actual.value()).isEqualTo(900);
    }
}
