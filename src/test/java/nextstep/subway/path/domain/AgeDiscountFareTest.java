package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgeDiscountFareTest {
    private final int none = -1;
    private final int children = 8;
    private final int youth = 15;
    private final int adult  = 25;
    private final int baseFare = 1_250;

    @Test
    @DisplayName("연령별 운임비용 할인: 청소년-350원을 공제한 금액의 20%할인, 어린이-350원을 공제한 금액의 50%할인")
    void getFareByAgeBased() {
        // when
        int childrenFare = AgeDiscountFare.calculate(baseFare, children);
        int youthFare = AgeDiscountFare.calculate(baseFare, youth);
        int adultFare = AgeDiscountFare.calculate(baseFare, adult);
        int noneFare = AgeDiscountFare.calculate(baseFare, none);

        // then
        assertThat(childrenFare).isEqualTo(450);
        assertThat(youthFare).isEqualTo(720);
        assertThat(adultFare).isEqualTo(baseFare);
        assertThat(noneFare).isEqualTo(baseFare);
    }
}
