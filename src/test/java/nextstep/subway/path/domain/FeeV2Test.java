package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeeV2Test {
    private FeeV2 feeV2;

    @BeforeEach
    void setUp() {
        feeV2 = new FeeV2();
    }

    @Test
    @DisplayName("요금 객체가 같은지 검증")
    void verifySameFee() {
        assertThat(feeV2).isEqualTo(new FeeV2());
    }

    @Test
    @DisplayName("더한 금액이 잘 반영되는지 검증")
    void addExtraFee() {
        feeV2.add(500);

        assertThat(feeV2.getFee()).isEqualTo(1750);
    }

    @Test
    @DisplayName("금액이 업데이트되는지 확인")
    void updateFee() {
        feeV2.update(500);

        assertThat(feeV2.getFee()).isEqualTo(500);
    }
}
