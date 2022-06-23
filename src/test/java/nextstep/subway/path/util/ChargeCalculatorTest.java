package nextstep.subway.path.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ChargeCalculatorTest 클래스")
public class ChargeCalculatorTest {

    @DisplayName("거리가 8일 때 지하철 요금은 1,250원을 반환한다.")
    @Test
    void calculateLessOrEqualTen() {
        //given
        final int distance = 8;

        //when
        final int 지하철_요금 = ChargeCalculator.calculate(distance);

        //then
        지하철_요금_확인(지하철_요금, 1_250);
    }

    @DisplayName("거리가 17일 때 지하철 요금은 1,650원을 반환한다.")
    @Test
    void calculateBetweenTenAndFifty() {
        //given
        final int distance = 17;

        //when
        final int 지하철_요금 = ChargeCalculator.calculate(distance);

        //then
        지하철_요금_확인(지하철_요금, 1_650);
    }

    @DisplayName("거리가 58일 때 지하철 요금은 2,050원을 반환한다.")
    @Test
    void calculateMoreOrEqualFifty() {
        //given
        final int distance = 58;

        //when
        final int 지하철_요금 = ChargeCalculator.calculate(distance);

        //then
        지하철_요금_확인(지하철_요금, 2_050);
    }

    private void 지하철_요금_확인(final int totalCharge, final int expected) {
        assertThat(totalCharge).isEqualTo(expected);
    }
}
