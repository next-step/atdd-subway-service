package nextstep.subway.fare;

import nextstep.subway.Fare.domain.Age;
import nextstep.subway.Fare.domain.Fare;
import nextstep.subway.Fare.domain.FareCalculator;
import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {
    private List<Line> lines = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Line 신분당선 = new Line("신분당선", "bg-red-600");
        신분당선.addAdditionalFare(100);
        Line 경의중앙선 = new Line("경의중앙선", "bg-green-600");
        경의중앙선.addAdditionalFare(200);
        lines.add(신분당선);
        lines.add(경의중앙선);
    }

    @DisplayName("10Km 이하 요금 계산")
    @Test
    void calculatorFare_down_10Km() {
        // given
        Fare fare = 요금계산(lines, 5L, Age.ADULT);

        // then
        assertThat(fare.getValue()).isEqualTo(1450);
    }

    @DisplayName("10Km 이상 50Km 이하 요금 계산")
    @Test
    void calculatorFare_up_10Km_and_down_50km() {
        // given
        Fare fare = 요금계산(lines, 50L, Age.ADULT);

        // then
        assertThat(fare.getValue()).isEqualTo(2250);
    }

    @DisplayName("50Km 초과 요금 계산")
    @Test
    void calculatorFare_up_50km() {
        // given
        Fare fare = 요금계산(lines, 80L, Age.ADULT);

        // then
        assertThat(fare.getValue()).isEqualTo(2650);
    }

    @DisplayName("어린이 할인")
    @Test
    void calculatorFare_child() {
        // given
        Fare fare = 요금계산(lines, 80L, Age.CHILD);

        // then
        assertThat(fare.getValue()).isEqualTo(1150);
    }

    @DisplayName("청소년 할인")
    @Test
    void calculatorFare_teenager() {
        // given
        Fare fare = 요금계산(lines, 80L, Age.TEENAGER);

        // then
        assertThat(fare.getValue()).isEqualTo(1840);
    }

    @DisplayName("일반 성인")
    @Test
    void calculatorFare_adult() {
        // given
        Fare fare = 요금계산(lines, 80L, Age.ADULT);

        // then
        assertThat(fare.getValue()).isEqualTo(2650);
    }

    public static Fare 요금계산(List<Line> lines, Long distance, Age age) {
        return FareCalculator.calculate(lines, distance, age);
    }
}
