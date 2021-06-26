package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {

    private FareCalculator fareCalculator;

    @Test
    @DisplayName("일반인 10km 이하")
    void 일반인_10km_이하() {
        fareCalculator = new FareCalculator(5, 30, 0);
        assertThat(fareCalculator.calculate()).isEqualTo(1250);
    }

    @Test
    @DisplayName("일반인 10km초과 50km 미만")
    void 일반인_10km_초과_50km_미만() {
        fareCalculator = new FareCalculator(26, 30, 0);
        assertThat(fareCalculator.calculate()).isEqualTo(1650);
    }

    @Test
    @DisplayName("일반인 50km 이상")
    void 일반인_50km_이상() {
        fareCalculator = new FareCalculator(67, 30, 0);
        assertThat(fareCalculator.calculate()).isEqualTo(2350);
    }

    @Test
    @DisplayName("일반인 10km 이하 추가금액 400원")
    void 일반인_10km_이하_추가금액_400원() {
        fareCalculator = new FareCalculator(5, 30, 400);
        assertThat(fareCalculator.calculate()).isEqualTo(1650);
    }

    @Test
    @DisplayName("청소년 10km 이하")
    void 청소년_10km_이하() {
        fareCalculator = new FareCalculator(5, 17, 0);
        assertThat(fareCalculator.calculate()).isEqualTo(720);
    }

    @Test
    @DisplayName("청소년 10km초과 50km 미만")
    void 청소년_10km_초과_50km_미만() {
        fareCalculator = new FareCalculator(26, 17, 0);
        assertThat(fareCalculator.calculate()).isEqualTo(1040);
    }

    @Test
    @DisplayName("청소년 50km 이상")
    void 청소년_50km_이상() {
        fareCalculator = new FareCalculator(67, 17, 0);
        assertThat(fareCalculator.calculate()).isEqualTo(1600);
    }

    @Test
    @DisplayName("청소년 10km초과 50km 미만 추가금액 400원")
    void 청소년_10km_초과_50km_미만_추가금액_400원() {
        fareCalculator = new FareCalculator(26, 17, 400);
        assertThat(fareCalculator.calculate()).isEqualTo(1360);
    }

    @Test
    @DisplayName("어린이 10km 이하")
    void 어린이_10km_이하() {
        fareCalculator = new FareCalculator(5, 8, 0);
        assertThat(fareCalculator.calculate()).isEqualTo(450);
    }

    @Test
    @DisplayName("어린이 10km초과 50km 미만")
    void 어린이_10km_초과_50km_미만() {
        fareCalculator = new FareCalculator(26, 8, 0);
        assertThat(fareCalculator.calculate()).isEqualTo(650);
    }

    @Test
    @DisplayName("어린이 50km 이상")
    void 어린이_50km_이상() {
        fareCalculator = new FareCalculator(67, 8, 0);
        assertThat(fareCalculator.calculate()).isEqualTo(1000);
    }

    @Test
    @DisplayName("어린이 50km 이상 추가금액 400원")
    void 어린이_50km_이상_추가금액_400원() {
        fareCalculator = new FareCalculator(67, 8, 400);
        assertThat(fareCalculator.calculate()).isEqualTo(1200);
    }
}
