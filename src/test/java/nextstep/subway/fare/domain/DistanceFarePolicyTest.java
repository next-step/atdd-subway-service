package nextstep.subway.fare.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;

@DisplayName("거리별 추가 요금 계산")
class DistanceFarePolicyTest {
    private Fare 기본요금;

    @DisplayName("거리가 10km 이하인 경우 기본요금만 나온다.")
    void under10km() {
        Fare 최소 = DistanceFarePolicy.calculate(1);
        Fare 최대 = DistanceFarePolicy.calculate(10);
        assertThat(최소).isEqualTo(Fare.from(1250));
        assertThat(최대).isEqualTo(Fare.from(1250));
    }


    @DisplayName("거리가 10km 초과, 50km 이하인 경우 5km 마다 100원씩 추가된다.")
    void 거리가_10km_초과_50km_이하() {
        Fare 초과최소 = DistanceFarePolicy.calculate(11);
        Fare 초과최대 = DistanceFarePolicy.calculate(15);
        assertThat(초과최소).isEqualTo(Fare.from(1350));
        assertThat(초과최대).isEqualTo(Fare.from(1350));

        Fare 다시초과 = DistanceFarePolicy.calculate(16);
        assertThat(다시초과).isEqualTo(Fare.from(1450));

    }

    @DisplayName("거리가 50km 초과인 경우 8km 마다 100원씩 추가된다.")
    void 거리가_50km_초과() {
        Fare 최소 = DistanceFarePolicy.calculate(51);
        Fare 최대 = DistanceFarePolicy.calculate(58);
        assertThat(최소).isEqualTo(Fare.from(2150));
        assertThat(최대).isEqualTo(Fare.from(2150));
    }
}
