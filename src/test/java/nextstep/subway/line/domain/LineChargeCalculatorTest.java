package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import nextstep.subway.line.application.LineChargeCalculator;
import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineChargeCalculatorTest {

    @Test
    @DisplayName("구간 추가 요금 계산")
    void calculate(){
        // given
        int charge = 1250;
        Line green = new Line("2호선", "green", null, null, 10, 200);
        Set<Line> lines = new HashSet<>();
        lines.add(green);

        // when
        int result = LineChargeCalculator.calculate(charge, lines);

        // then
        assertThat(result).isEqualTo(1450);
    }

    @Test
    @DisplayName("구간 추가 요금 계산 - 노선이 여러개면 가장 비싼 요금만 부과한다.")
    void calculate2(){
        // given
        int charge = 1250;
        Line green = new Line("2호선", "green", null, null, 10, 200);
        Line purple = new Line("5호선", "purple", null, null, 10, 500);
        Set<Line> lines = new HashSet<>();
        lines.add(green);
        lines.add(purple);

        // when
        int result = LineChargeCalculator.calculate(charge, lines);

        // then
        assertThat(result).isEqualTo(1750);
    }
}
