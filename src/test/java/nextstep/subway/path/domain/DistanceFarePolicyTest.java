package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceFarePolicyTest {
    @Test
    void 추가_요금_비교_50km_초과시() {
        int distance = 55;
        int overFare = DistanceFarePolicy.calculateOverFare(distance);

        assertThat(overFare).isEqualTo(700);
    }

    @Test
    void 추가_요금_비교_10km_초과시() {
        int distance = 15;
        int overFare = DistanceFarePolicy.calculateOverFare(distance);

        assertThat(overFare).isEqualTo(300);
    }

    @Test
    void 추가_요금_비교_10km_이내() {
        int distance = 10;
        int overFare = DistanceFarePolicy.calculateOverFare(distance);

        assertThat(overFare).isEqualTo(0);
    }
}
