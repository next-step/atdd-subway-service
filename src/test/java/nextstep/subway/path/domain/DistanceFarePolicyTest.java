package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceFarePolicyTest {
    private DistanceFarePolicy low;
    private DistanceFarePolicy middle;
    private DistanceFarePolicy high;

    @DisplayName("거리별 요금정책을 조회한다.")
    @Test
    void findByDistance() {
        //when
        거리별_요금정책_조회();

        //then
        assertThat(low.name()).isEqualTo("LOW");
        assertThat(middle.name()).isEqualTo("MIDDLE");
        assertThat(high.name()).isEqualTo("HIGH");
    }

    @DisplayName("비정상적인 거리의 요금정책을 조회한다.")
    @Test
    void invalid_findByDistance() {
        //when & then
        assertThatThrownBy(() -> {
            DistanceFarePolicy.findByDistance(-1);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비정상적인 이동 거리입니다.");
    }

    @DisplayName("거리별 요금정책에 요금을 계산한다.")
    @Test
    void calculate() {
        //given
        거리별_요금정책_조회();

        //when
        int lowFare = low.calculate(10);
        int middleFare = middle.calculate(11);
        int highFare = high.calculate(51);

        //then
        assertThat(lowFare).isEqualTo(1250);
        assertThat(middleFare).isEqualTo(1350);
        assertThat(highFare).isEqualTo(2150);
    }

    private void 거리별_요금정책_조회() {
        low = DistanceFarePolicy.findByDistance(10);
        middle = DistanceFarePolicy.findByDistance(11);
        high = DistanceFarePolicy.findByDistance(51);
    }
}
