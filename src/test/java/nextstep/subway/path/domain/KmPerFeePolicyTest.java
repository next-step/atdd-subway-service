package nextstep.subway.path.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class KmPerFeePolicyTest {

    @Test
    @DisplayName("10키로미터 이하일 경우 요금을 구한다")
    void kmPer10Fee() {
        FeeStrategy kmPerFeePolicy = new KmPerFeePolicy();
        int result = kmPerFeePolicy.calculate(10);
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("20킬로 미터 이상 경우 요금을 구한다")
    void kmPer20Fee() {
        FeeStrategy kmPerFeePolicy = new KmPerFeePolicy();
        int result = kmPerFeePolicy.calculate(20);
        assertThat(result).isEqualTo(1450);
    }

    @Test
    @DisplayName("50킬로 미터 이상 경우 요금을 구한다")
    void kmPer50Fee() {
        FeeStrategy kmPerFeePolicy = new KmPerFeePolicy();
        int result = kmPerFeePolicy.calculate(50);
        assertThat(result).isEqualTo(2050);
    }
}