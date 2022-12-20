package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Surcharge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("10km 미만 경로 요금 조회한다.")
    @Test
    void 거리_10KM_미만_금액_계산_테스트() {
        Fare fare = new Fare(new Distance(8), new Surcharge(0), null);

        assertThat(fare.value()).isEqualTo(1250);
    }

    @DisplayName("10km 이상 50km 미만 경로 요금 조회한다.")
    @Test
    void 거리_10KM_이상_50KM_미만_금액_계산_테스트() {
        Fare fare = new Fare(new Distance(45), new Surcharge(0), null);

        assertThat(fare.value()).isEqualTo(1950);
    }

    @DisplayName("50km 이상 경로 요금 조회한다.")
    @Test
    void 거리_50KM_이상_금액_계산_테스트() {
        Fare fare = new Fare(new Distance(88), new Surcharge(0), null);

        assertThat(fare.value()).isEqualTo(2550);
    }

    @DisplayName("10km 미만 추가요금 노선 경로 요금 조회한다.")
    @Test
    void 거리_10KM_미만_추가요금_합산_금액_계산_테스트() {
        Fare fare = new Fare(new Distance(8), new Surcharge(300), null);

        assertThat(fare.value()).isEqualTo(1550);
    }

    @DisplayName("10km 이상 50km 미만 추가요금 노선 경로 요금 조회한다.")
    @Test
    void 거리_10KM_이상_50KM_미만_추가요금_합산_금액_계산_테스트() {
        Fare fare = new Fare(new Distance(45), new Surcharge(300), null);

        assertThat(fare.value()).isEqualTo(2250);
    }

    @DisplayName("50km 이상 추가요금 노선 경로 요금 조회한다.")
    @Test
    void 거리_50KM_이상_추가요금_합산_금액_계산_테스트() {
        Fare fare = new Fare(new Distance(88), new Surcharge(300), null);

        assertThat(fare.value()).isEqualTo(2850);
    }

    @DisplayName("어린이 할인 시 경로 요금을 조회한다.")
    @Test
    void 어린이_요금_할인_계산테스트() {
        Fare fare = new Fare(new Distance(88), new Surcharge(300), 7);

        assertThat(fare.value()).isEqualTo(1250);
    }

    @DisplayName("청소년 할인 시 경로 요금을 조회한다.")
    @Test
    void 청소년_요금_할인_계산테스트() {
        Fare fare = new Fare(new Distance(88), new Surcharge(300), 17);

        assertThat(fare.value()).isEqualTo(2000);
    }
}
