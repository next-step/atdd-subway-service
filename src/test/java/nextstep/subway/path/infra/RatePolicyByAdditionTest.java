package nextstep.subway.path.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RatePolicyByAdditionTest {

    @DisplayName("추가 요금 정책 확인")
    @Test
    public void step1() {
        assertAll(
            () -> assertThat(new RatePolicyByAddition(0).calc(1250)).isEqualTo(1250),
            () -> assertThat(new RatePolicyByAddition(100).calc(1250)).isEqualTo(1350),
            () -> assertThat(new RatePolicyByAddition(150).calc(1250)).isEqualTo(1400)
        );
    }

}