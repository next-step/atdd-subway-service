package nextstep.subway.path.types;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ChargeType 클래스")
public class ChargeTypeTest {
    @DisplayName("거리가 8km일 때 (첫번째 구간) 지하철 요금은 1,250원을 반환한다.")
    @Test
    void calculateLevelOne() {
        //given
        final int distance = 8;

        //when
        final int 지하철_요금 = ChargeType.calculateChargeFrom(distance);

        //then
        지하철_요금_확인(지하철_요금, 1_250);
    }

    @DisplayName("거리가 37km일 때 (두번째 구간) 지하철 요금은 1,850원을 반환한다.")
    @Test
    void calculateLevelTwo() {
        //given
        final int distance = 37;

        //when
        final int 지하철_요금 = ChargeType.calculateChargeFrom(distance);

        //then
        지하철_요금_확인(지하철_요금, 1_850);
    }

    @DisplayName("거리가 71km일 때 (세번째 구간) 지하철 요금은 2,350원을 반환한다.")
    @Test
    void calculateLevelThree() {
        //given
        final int distance = 71;

        //when
        final int 지하철_요금 = ChargeType.calculateChargeFrom(distance);

        //then
        지하철_요금_확인(지하철_요금, 2_350);
    }

    private void 지하철_요금_확인(final int 지하철_요금, final int expected) {
        assertThat(지하철_요금).isEqualTo(expected);
    }
}
