package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {

    FareCalculator fareCalculator;

    @Test
    @DisplayName("일반인 10km 이하")
    void 일반인_10km_이하() {
        fareCalculator = new FareCalculator(5, 30);
        assertThat(fareCalculator.calculate()).isEqualTo(1250);
    }

    @Test
    @DisplayName("일반인 10km초과 50km 미만")
    void 일반인_10km_초과_50km_미만() {
        fareCalculator = new FareCalculator(26, 30);
        assertThat(fareCalculator.calculate()).isEqualTo(1650);
    }

    @Test
    @DisplayName("일반인 50km 이상")
    void 일반인_50km_이상() {
        fareCalculator = new FareCalculator(67, 30);
        assertThat(fareCalculator.calculate()).isEqualTo(2350);
    }

    @Test
    @DisplayName("청소년 10km 이하")
    void 청소년_10km_이하() {
        fareCalculator = new FareCalculator(5, 17);
        assertThat(fareCalculator.calculate()).isEqualTo(720);
    }

    @Test
    @DisplayName("청소년 10km초과 50km 미만")
    void 청소년_10km_초과_50km_미만() {
        fareCalculator = new FareCalculator(26, 17);
        assertThat(fareCalculator.calculate()).isEqualTo(1040);
    }

    @Test
    @DisplayName("청소년 50km 이상")
    void 청소년_50km_이상() {
        fareCalculator = new FareCalculator(67, 17);
        assertThat(fareCalculator.calculate()).isEqualTo(1600);
    }

    @Test
    @DisplayName("어린이 10km 이하")
    void 어린이_10km_이하() {
        fareCalculator = new FareCalculator(5, 8);
        assertThat(fareCalculator.calculate()).isEqualTo(450);
    }

    @Test
    @DisplayName("어린이 10km초과 50km 미만")
    void 어린이_10km_초과_50km_미만() {
        fareCalculator = new FareCalculator(26, 8);
        assertThat(fareCalculator.calculate()).isEqualTo(650);
    }

    @Test
    @DisplayName("어린이 50km 이상")
    void 어린이_50km_이상() {
        fareCalculator = new FareCalculator(67, 8);
        assertThat(fareCalculator.calculate()).isEqualTo(1000);
    }
}
