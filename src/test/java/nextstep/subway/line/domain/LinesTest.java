package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 일급 콜렉션")
class LinesTest {

    @DisplayName("구간 요금이 가장 높은 구간의 요금을 금액 계산")
    @Test
    void linesFare() {
        Lines lines = new Lines();
        Line lineA = Line.of("A", "blue", 900);
        Line lineB = Line.of("B", "red", 500);
        lines.addAll(Arrays.asList(lineA, lineB));
        assertThat(lines.addFare()).isEqualTo(900);
    }
}
