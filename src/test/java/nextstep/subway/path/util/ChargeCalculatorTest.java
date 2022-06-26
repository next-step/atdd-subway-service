package nextstep.subway.path.util;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ChargeCalculatorTest 클래스")
public class ChargeCalculatorTest {

    @DisplayName("거리가 8이고 노선 추가요금이 0일 때 지하철 요금은 1,250원을 반환한다.")
    @Test
    void calculateLessOrEqualTen() {
        //given
        final int distance = 8;
        final int surcharge = 0;

        //when
        final int 지하철_요금 = ChargeCalculator.calculateTotalCharge(distance, surcharge, LoginMember.empty());

        //then
        지하철_요금_확인(지하철_요금, 1_250);
    }

    @DisplayName("거리가 17이고 노선 추가요금이 0일 때 지하철 요금은 1,450원을 반환한다.")
    @Test
    void calculateBetweenTenAndFifty() {
        //given
        final int distance = 17;
        final int surcharge = 0;

        //when
        final int 지하철_요금 = ChargeCalculator.calculateTotalCharge(distance, surcharge, LoginMember.empty());

        //then
        지하철_요금_확인(지하철_요금, 1_450);
    }

    @DisplayName("거리가 58이고 노선 추가요금이 0일 때 지하철 요금은 2,150원을 반환한다.")
    @Test
    void calculateMoreOrEqualFifty() {
        //given
        final int distance = 58;
        final int surcharge = 0;

        //when
        final int 지하철_요금 = ChargeCalculator.calculateTotalCharge(distance, surcharge, LoginMember.empty());

        //then
        지하철_요금_확인(지하철_요금, 2_150);
    }

    private void 지하철_요금_확인(final int totalCharge, final int expected) {
        assertThat(totalCharge).isEqualTo(expected);
    }
}
