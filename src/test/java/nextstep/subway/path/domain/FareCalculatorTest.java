package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 운임 비용을 계산하는 클래스")
class FareCalculatorTest {
    @Test
    void 어른인_회원의_지하철_운임_비용을_구함() {
        assertEquals(1950, FareCalculator.calculate(20, 1450, 500));
    }

    @Test
    void 어린이인_회원의_지하철_운임_비용을_구함() {
        assertEquals(1050, FareCalculator.calculate(8, 2250, 200));
    }

    @Test
    void 청소년인_회원의_지하철_운임_비용을_구함() {
        assertEquals(1760, FareCalculator.calculate(17, 2250, 300));
    }

    @Test
    void 비회원의_지하철_운임_비용을_구함() {
        assertEquals(3050, FareCalculator.calculate(0, 2950, 100));
    }
}
