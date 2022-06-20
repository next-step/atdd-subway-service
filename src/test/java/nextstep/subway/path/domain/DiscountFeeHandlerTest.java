package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountFeeHandlerTest {
    private FeeV2 feeV2;
    private FeeHandler feeHandler;

    @BeforeEach
    void setUp() {
        feeV2 = new FeeV2();
    }

    @ParameterizedTest(name = "청소년({0}) 요금할인이 잘 적용되는지 검증")
    @ValueSource(ints = {13, 18})
    void discountTeenFee(int age) {
        feeHandler = new DiscountFeeHandler(null, age);
        feeHandler.calculate(feeV2);

        assertThat(feeV2.getFee()).isEqualTo(720);
    }

    @ParameterizedTest(name = "어린이({0}) 요금할인이 잘 적용되는지 검증")
    @ValueSource(ints = {6, 12})
    void discountChildFee(int age) {
        feeHandler = new DiscountFeeHandler(null, age);
        feeHandler.calculate(feeV2);

        assertThat(feeV2.getFee()).isEqualTo(450);
    }

    @ParameterizedTest(name = "청소년, 어린이 외는 요금할인이 적용되지 않는지 검증")
    @ValueSource(ints = {5, 19})
    void notDiscountOtherFee(int age) {
        feeHandler = new DiscountFeeHandler(null, age);
        feeHandler.calculate(feeV2);

        assertThat(feeV2.getFee()).isEqualTo(1250);
    }
}
