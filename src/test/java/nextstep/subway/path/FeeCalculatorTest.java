package nextstep.subway.path;

import nextstep.subway.member.domain.NoDiscountStrategy;
import nextstep.subway.member.dto.Money;
import nextstep.subway.path.application.FeeCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FeeCalculatorTest {

    @DisplayName("기본 운임 테스트")
    @Test
    public void defaultFee() {
        Money fee = FeeCalculator.calculate(Money.of(0), 0, new NoDiscountStrategy());
        assertThat(fee).isEqualTo(Money.of(1250));

        Money fee2 = FeeCalculator.calculate(Money.of(0),10, new NoDiscountStrategy());
        assertThat(fee2).isEqualTo(Money.of(1250));
    }

    @DisplayName("10~50키로 테스트")
    @Test
    public void midFee() {
        Money fee = FeeCalculator.calculate(Money.of(0),15, new NoDiscountStrategy());
        assertThat(fee).isEqualTo(Money.of(1350));

        Money fee2 = FeeCalculator.calculate(Money.of(0),50, new NoDiscountStrategy());
        assertThat(fee2).isEqualTo(Money.of(2050));
    }

    @DisplayName("50키로 초과시")
    @Test
    public void longFee() {
        Money fee = FeeCalculator.calculate(Money.of(0),130, new NoDiscountStrategy());
        assertThat(fee).isEqualTo(Money.of(3050));
    }

    @DisplayName("추가요금 있는 경우 10~50키로 테스트")
    @Test
    public void midFeeWithExtraCharge() {
        Money fee = FeeCalculator.calculate(Money.of(2000),15, new NoDiscountStrategy());
        assertThat(fee).isEqualTo(Money.of(3350));

        Money fee2 = FeeCalculator.calculate(Money.of(20),50, new NoDiscountStrategy());
        assertThat(fee2).isEqualTo(Money.of(2070));
    }


}
