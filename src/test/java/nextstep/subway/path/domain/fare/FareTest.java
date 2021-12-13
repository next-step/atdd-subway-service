package nextstep.subway.path.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 요금 기능")
class FareTest {

    @Test
    @DisplayName("거리에 따른 추가 운임을 계산한다.")
    void calculateOverFare() {
        assertThat(new Fare(10).getFare()).isEqualTo(1250); //+0
        assertThat(new Fare(14).getFare()).isEqualTo(1350); //+100

        assertThat(new Fare(20).getFare()).isEqualTo(1450); //+200
        assertThat(new Fare(26).getFare()).isEqualTo(1650); //+400

        assertThat(new Fare(55).getFare()).isEqualTo(1850); //+600
        assertThat(new Fare(58).getFare()).isEqualTo(1850); //+600
        assertThat(new Fare(59).getFare()).isEqualTo(1950); //+700
    }
}
