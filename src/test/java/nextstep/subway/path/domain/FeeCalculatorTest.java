package nextstep.subway.path.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FeeCalculatorTest {

    @Test
    @DisplayName("10KM 이하로 지하철을 탔을경우 요금을 구한다")
    void kmPer10ByFeePolicy() {
        int fee = FeeCalculator.from(0, 10).getFee(new KmPerFeePolicy());

        assertThat(fee).isEqualTo(1250);
    }

    @Test
    @DisplayName("30KM 지하철을 탔을경우 요금을 구한다")
    void kmPer30ByFeePolicy() {
        int fee = FeeCalculator.from(0, 30).getFee(new KmPerFeePolicy());

        assertThat(fee).isEqualTo(1650);
    }

    @Test
    @DisplayName("50KM 지하철을 탔을경우 요금을 구한다")
    void kmPer50ByFeePolicy() {
        int fee = FeeCalculator.from(0, 50).getFee(new KmPerFeePolicy());

        assertThat(fee).isEqualTo(2050);
    }
}