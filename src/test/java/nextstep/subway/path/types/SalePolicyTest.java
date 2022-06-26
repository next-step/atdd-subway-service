package nextstep.subway.path.types;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SalePolicy 클래스")
public class SalePolicyTest {
    @DisplayName("총 금액이 1,350원이고 나이가 6세(어린이)일 때 지하철 요금은 500원을 반환한다.")
    @Test
    void calculateChild() {
        //given
        final int totalCharge = 1_350;
        final int age = 6;

        //when
        final int 지하철_요금 = SalePolicy.calculateAgeDiscountFrom(totalCharge, age);

        //then
        지하철_요금_확인(지하철_요금, 500);
    }

    @DisplayName("총 금액이 1,350원이고 나이가 15세(청소년)일 때 지하철 요금은 800원을 반환한다.")
    @Test
    void calculateYouth() {
        //given
        final int totalCharge = 1_350;
        final int age = 15;

        //when
        final int 지하철_요금 = SalePolicy.calculateAgeDiscountFrom(totalCharge, age);

        //then
        지하철_요금_확인(지하철_요금, 800);
    }

    @DisplayName("총 금액이 1,350원이고 나이가 20세(성인)일 때 지하철 요금은 1,350원을 반환한다.")
    @Test
    void calculateAdult() {
        //given
        final int totalCharge = 1_350;
        final int age = 20;

        //when
        final int 지하철_요금 = SalePolicy.calculateAgeDiscountFrom(totalCharge, age);

        //then
        지하철_요금_확인(지하철_요금, 1_350);
    }

    private void 지하철_요금_확인(final int 지하철_요금, final int expected) {
        assertThat(지하철_요금).isEqualTo(expected);
    }
}
