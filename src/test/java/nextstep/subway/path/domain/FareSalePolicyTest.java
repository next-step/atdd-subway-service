package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareSalePolicyTest {
    private final int BASIC_FARE = 1250;
    @Test
    void 청소년_기본요금일때_할인되는_금액() {
        int age = 18;
        int sale = FareSalePolicy.calculateSaleByAge(age, BASIC_FARE);
        assertThat(sale).isEqualTo(180);
    }

    @Test
    void 어린이_기본요금일때_할인되는_금액() {
        int age = 6;
        int sale = FareSalePolicy.calculateSaleByAge(age, BASIC_FARE);
        assertThat(sale).isEqualTo(450);
    }
}
