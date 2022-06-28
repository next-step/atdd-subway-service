package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceFarePolicyTest {

    @Test
    @DisplayName("10키로 미만 추가 요금")
    void test() {
        // when
        final int extraCharge = DistanceFarePolicy.ofExtraCharge(10);
        // then
        assertThat(extraCharge).isEqualTo(0);
    }

    @Test
    @DisplayName("10 ~ 50 키로 추가 요금")
    void test2() {
        // when
        final int extraCharge = DistanceFarePolicy.ofExtraCharge(15);
        // then
        assertThat(extraCharge).isEqualTo(100);
    }

    @Test
    @DisplayName("50키로 추가 요금")
    void test3() {
        // when
        final int extraCharge = DistanceFarePolicy.ofExtraCharge(60);
        // then
        assertThat(extraCharge).isEqualTo(1000);
    }
}
