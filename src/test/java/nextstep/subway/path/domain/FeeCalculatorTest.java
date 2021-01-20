package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FeeCalculatorTest {
    @Test
    @DisplayName("9키로 이동시 성인요금 1250")
    void defaultFee() {
        Integer fee = FeeCalculator.calculateFee(9);
        assertThat(fee).isEqualTo(1250);
    }

    @Test
    @DisplayName("30키로 이동시 성인기본요금(1250) + 추가요금(400) = 1650")
    void extraFeeUnder50() {
        Integer fee = FeeCalculator.calculateFee(30);
        assertThat(fee).isEqualTo(1650);
    }

    @Test
    @DisplayName("60키로 이동시 성인기본요금(1250) + 50키로이내 추가요금(800) + 50키로초과 추가요금(200) = 2250")
    void extraFeeOver60() {
        Integer fee = FeeCalculator.calculateFee(60);
        assertThat(fee).isEqualTo(2250);
    }
}