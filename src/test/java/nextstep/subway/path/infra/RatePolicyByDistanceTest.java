package nextstep.subway.path.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class RatePolicyByDistanceTest {

    @DisplayName("10~50km 이내 : 5km 까지 마다 100원 추가")
    @Test
    public void step1() {
        assertAll(
            () -> assertThat(new RatePolicyByDistance(10).calc()).isEqualTo(1250),
            () -> assertThat(new RatePolicyByDistance(15).calc()).isEqualTo(1350),
            () -> assertThat(new RatePolicyByDistance(20).calc()).isEqualTo(1450),
            () -> assertThat(new RatePolicyByDistance(25).calc()).isEqualTo(1550),
            () -> assertThat(new RatePolicyByDistance(30).calc()).isEqualTo(1650)
        );
    }

    @DisplayName("50km 초과 : 8km 까지 마다 100원 추가")
    @Test
    public void setp2() {
        assertAll(
            () -> assertThat(new RatePolicyByDistance(50).calc()).isEqualTo(2050),
            () -> assertThat(new RatePolicyByDistance(58).calc()).isEqualTo(2150),
            () -> assertThat(new RatePolicyByDistance(66).calc()).isEqualTo(2250),
            () -> assertThat(new RatePolicyByDistance(74).calc()).isEqualTo(2350),
            () -> assertThat(new RatePolicyByDistance(82).calc()).isEqualTo(2450),
            () -> assertThat(new RatePolicyByDistance(90).calc()).isEqualTo(2550),
            () -> assertThat(new RatePolicyByDistance(98).calc()).isEqualTo(2650),
            () -> assertThat(new RatePolicyByDistance(106).calc()).isEqualTo(2750),
            () -> assertThat(new RatePolicyByDistance(114).calc()).isEqualTo(2850),
            () -> assertThat(new RatePolicyByDistance(122).calc()).isEqualTo(2950),
            () -> assertThat(new RatePolicyByDistance(130).calc()).isEqualTo(3050),
            () -> assertThat(new RatePolicyByDistance(138).calc()).isEqualTo(3150),
            () -> assertThat(new RatePolicyByDistance(146).calc()).isEqualTo(3250),
            () -> assertThat(new RatePolicyByDistance(154).calc()).isEqualTo(3350),
            () -> assertThat(new RatePolicyByDistance(162).calc()).isEqualTo(3450),
            () -> assertThat(new RatePolicyByDistance(170).calc()).isEqualTo(3550),
            () -> assertThat(new RatePolicyByDistance(178).calc()).isEqualTo(3650)
        );
    }

}