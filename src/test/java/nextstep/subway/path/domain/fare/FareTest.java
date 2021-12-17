package nextstep.subway.path.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 요금 기능")
class FareTest {

    @Test
    @DisplayName("거리에 따른 추가요금을 계산한다.")
    void calculateDistanceFare() {
        Fare fare = new Fare();
        assertThat(fare.getMoney()).isEqualTo(1250); //+0

        assertThat(fare.extraFare(10, 0)).isEqualTo(new Fare(1250)); //+0
        assertThat(fare.extraFare(14, 0)).isEqualTo(new Fare(1350)); //+100

        assertThat(fare.extraFare(20, 0)).isEqualTo(new Fare(1450)); //+200
        assertThat(fare.extraFare(26, 0)).isEqualTo(new Fare(1650)); //+400

        assertThat(fare.extraFare(55, 0)).isEqualTo(new Fare(1850)); //+600
        assertThat(fare.extraFare(58, 0)).isEqualTo(new Fare(1850)); //+600
        assertThat(fare.extraFare(59, 0)).isEqualTo(new Fare(1950)); //+700
    }

    @Test
    @DisplayName("노선에 따른 추가요금을 계산한다.")
    void calculateLineFare() {
        Fare fare = new Fare();
        assertThat(fare.getMoney()).isEqualTo(1250); //+0

        assertThat(fare.extraFare(10, 500)).isEqualTo(new Fare(1750)); //+500
        assertThat(fare.extraFare(10, 900)).isEqualTo(new Fare(2150)); //+900

        assertThat(fare.extraFare(20, 500)).isEqualTo(new Fare(1950)); //+700
        assertThat(fare.extraFare(20, 900)).isEqualTo(new Fare(2350)); //+1100
    }

    @Test
    @DisplayName("나이에 따른 할인요금을 계산한다.")
    void calculateAgeFare() {
        Fare fare = new Fare();
        assertThat(fare.getMoney()).isEqualTo(1250); //+0

        assertThat(fare.discount(5)).isEqualTo(new Fare(0)); //-1250
        assertThat(fare.discount(12)).isEqualTo(new Fare(450)); //-800
        assertThat(fare.discount(18)).isEqualTo(new Fare(720)); //-530
        assertThat(fare.discount(19)).isEqualTo(new Fare(1250)); //-0
        assertThat(fare.discount(65)).isEqualTo(new Fare(0)); //-1250
    }
}
