package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    @DisplayName("요금은 0원 이상이어야만 한다.")
    void minFare() {
        //given
        int fare = -1;

        //when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> Fare.of(fare)
        );
    }

    @Test
    @DisplayName("기본거리의 요금을 계산한다.")
    void dealtFare() {
        Distance distance = Distance.of(8);

        assertThat(Fare.calculateFare(distance)).isEqualTo(Fare.of(1_250));
    }

    @Test
    @DisplayName("추가요금거리의 요금을 계산한다.")
    void shortFare() {
        Distance distance = Distance.of(36);

        assertThat(Fare.calculateFare(distance)).isEqualTo(Fare.of(1_850));
    }

    @Test
    @DisplayName("장거리의 요금을 계산한다.")
    void longFare() {
        Distance distance = Distance.of(55);

        assertThat(Fare.calculateFare(distance)).isEqualTo(Fare.of(2_150));
    }

}